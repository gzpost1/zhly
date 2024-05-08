package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.param.assembler.DepartmentConverter;
import cn.cuiot.dmp.system.application.param.assembler.MenuConverter;
import cn.cuiot.dmp.system.application.service.BusinessTypeService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 供其他服务调用接口
 *
 * @author: wuyongchong
 * @date: 2024/4/25 14:44
 */
@RestController
@RequestMapping("/api")
public class ApiController {


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentConverter departmentConverter;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuConverter menuConverter;

    @Autowired
    private BusinessTypeService businessTypeService;

    /**
     * 获取部门信息
     */
    @GetMapping(value = "/lookUpDepartmentInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<DepartmentDto> lookUpDepartmentInfo(
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "orgId", required = false) Long orgId) {
        DepartmentDto departmentDto = null;
        if (Objects.nonNull(deptId)) {
            DepartmentEntity departmentEntity = departmentService.getDeptById(deptId.toString());
            if (Objects.isNull(departmentEntity)) {
                departmentDto = departmentConverter.entityToDTO(departmentEntity);
            }
        }
        if (Objects.isNull(departmentDto) && Objects.nonNull(userId)) {
            departmentDto = departmentService.getPathByUser(userId);
        }
        if (Objects.isNull(departmentDto) && Objects.nonNull(orgId)) {
            List<DepartmentEntity> entityList = departmentService.getDeptRootByOrgId(orgId.toString());
            if (CollectionUtils.isNotEmpty(entityList)) {
                departmentDto = departmentConverter.entityToDTO(entityList.get(0));
            }
        }
        return IdmResDTO.success(departmentDto);
    }

    /**
     * 获取权限信息
     */
    @GetMapping(value = "/lookUpPermission", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<MenuDTO> lookUpPermission(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "orgId", required = false) String orgId,
            @RequestParam(value = "permissionCode", required = false) String permissionCode) {
        MenuDTO menuDTO = null;
        MenuEntity menuEntity = menuService.lookUpPermission(userId, orgId, permissionCode);
        if (Objects.nonNull(menuEntity)) {
            menuDTO = menuConverter.entityToDTO(menuEntity);
        }
        return IdmResDTO.success(menuDTO);
    }

    /**
     * 根据业务类型id列表获取业务类型列表（流程/工单配置）
     */
    @PostMapping(value = "/batchGetBusinessType", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<BusinessTypeRspDTO>> batchGetBusinessType(@RequestBody @Valid BusinessTypeReqDTO businessTypeReqDTO) {
        List<BusinessTypeRspDTO> businessTypeRspDTOList = businessTypeService.batchGetBusinessType(businessTypeReqDTO);
        return IdmResDTO.success(businessTypeRspDTOList);
    }

}
