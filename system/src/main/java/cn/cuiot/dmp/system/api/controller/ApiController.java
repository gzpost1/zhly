package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseRoleReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.param.assembler.DepartmentConverter;
import cn.cuiot.dmp.system.application.param.assembler.MenuConverter;
import cn.cuiot.dmp.system.application.service.*;
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
@InternalApi
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

    @Autowired
    private FormConfigService formConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色
     */
    @PostMapping(value = "/lookUpRoleList", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<BaseRoleDto>> lookUpRoleList(@RequestBody BaseRoleReqDto query) {
        List<BaseRoleDto> list = roleService.lookUpRoleList(query);
        return IdmResDTO.success(list);
    }

    /**
     * 查询部门
     */
    @PostMapping(value = "/lookUpDepartmentList", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<DepartmentDto>> lookUpDepartmentList(@RequestBody  DepartmentReqDto query) {
        List<DepartmentDto> list = departmentService.lookUpDepartmentList(query);
        return IdmResDTO.success(list);
    }

    /**
     * 查询用户
     */
    @PostMapping(value = "/lookUpUserList", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<BaseUserDto>> lookUpUserList(@RequestBody BaseUserReqDto query) {
        List<BaseUserDto> list = userService.lookUpUserList(query);
        return IdmResDTO.success(list);
    }

    /**
     * 获取用户信息
     */
    @PostMapping(value = "/lookUpUserInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<BaseUserDto> lookUpUserInfo(@RequestBody BaseUserReqDto query) {
        BaseUserDto dto = userService.lookUpUserInfo(query);
        return IdmResDTO.success(dto);
    }

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
     * 查询子部门
     */
    @PostMapping(value = "/lookUpDepartmentChildList", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<DepartmentDto>> lookUpDepartmentChildList(@RequestBody  DepartmentReqDto query) {
        List<DepartmentDto> list = departmentService.lookUpDepartmentChildList(query);
        return IdmResDTO.success(list);
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
        }else{
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
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

    /**
     * 批量查询表单配置
     */
    @PostMapping(value = "/batchQueryFormConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<FormConfigRspDTO>> batchQueryFormConfig(@RequestBody @Valid FormConfigReqDTO formConfigReqDTO) {
        List<FormConfigRspDTO> formConfigRspDTOList = formConfigService.batchQueryFormConfig(formConfigReqDTO);
        return IdmResDTO.success(formConfigRspDTOList);
    }

}
