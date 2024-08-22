package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.*;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.system.application.param.assembler.DepartmentConverter;
import cn.cuiot.dmp.system.application.param.assembler.MenuConverter;
import cn.cuiot.dmp.system.application.param.dto.FormConfigDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.*;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionSettingEntity;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionSettingMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private CustomConfigService customConfigService;

    @Autowired
    private AuditConfigTypeService auditConfigTypeService;
    @Autowired
    private CommonOptionSettingMapper commonOptionSettingMapper;

    @Autowired
    private UserHouseAuditService userHouseAuditService;

    @Autowired
    private PlatfromInfoService platfromInfoService;

    /**
     * 获取权限菜单
     */
    @GetMapping(value = "/getPermissionMenus", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<MenuEntity>> getPermissionMenus(
            @RequestParam(value = "orgId", required = false) String orgId,
            @RequestParam(value = "userId", required = false) String userId) {
        List<MenuEntity> menuList = menuService.getPermissionMenus(orgId, userId);
        return IdmResDTO.success(menuList);
    }

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
    public IdmResDTO<List<DepartmentDto>> lookUpDepartmentList(@RequestBody DepartmentReqDto query) {
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
            if (Objects.nonNull(departmentEntity)) {
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
    public IdmResDTO<List<DepartmentDto>> lookUpDepartmentChildList(@RequestBody DepartmentReqDto query) {
        List<DepartmentDto> list = departmentService.lookUpDepartmentChildList(query);
        return IdmResDTO.success(list);
    }

    /**
     * 查询子部门(可多部门查询)
     */
    @PostMapping(value = "/lookUpDepartmentChildList2", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<DepartmentDto>> lookUpDepartmentChildList2(@RequestBody DepartmentReqDto query) {
        List<DepartmentDto> list = departmentService.lookUpDepartmentChildList2(query);
        return IdmResDTO.success(list);
    }

    /**
     * 查询组织树
     */
    @PostMapping(value = "/lookUpDepartmentTree", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<DepartmentTreeRspDTO>> lookUpDepartmentTree(
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "userId", required = false) Long userId) {
        List<DepartmentTreeVO> list = departmentService.getDepartmentTree(orgId.toString(), userId.toString(), null);
        if (CollectionUtils.isEmpty(list)) {
            return IdmResDTO.success(new ArrayList<>());
        }
        List<DepartmentTreeRspDTO> departmentTreeRspDTOList = new ArrayList<>();
        DepartmentTreeRspDTO departmentTreeRspDTO = departmentService.getDepartmentTreeRspDTO(list.get(0));
        departmentTreeRspDTOList.add(departmentTreeRspDTO);
        return IdmResDTO.success(departmentTreeRspDTOList);
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
        } else {
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
     * 通过名称查询表单配置详情
     */
    @PostMapping(value = "/lookUpFormConfigByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<FormConfigRspDTO> lookUpFormConfigByName(@RequestBody @Valid FormConfigReqDTO formConfigReqDTO) {
        FormConfigDTO formConfigDTO = new FormConfigDTO();
        formConfigDTO.setName(formConfigReqDTO.getName());
        formConfigDTO.setCompanyId(formConfigReqDTO.getCompanyId());
        FormConfigVO formConfigVO = formConfigService.queryForDetailByName(formConfigDTO);
        FormConfigRspDTO formConfigRspDTO = new FormConfigRspDTO();
        BeanUtils.copyProperties(formConfigVO, formConfigRspDTO);
        return IdmResDTO.success(formConfigRspDTO);
    }

    /**
     * 批量查询表单配置
     */
    @PostMapping(value = "/batchQueryFormConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<FormConfigRspDTO>> batchQueryFormConfig(@RequestBody @Valid FormConfigReqDTO formConfigReqDTO) {
        List<FormConfigRspDTO> formConfigRspDTOList = formConfigService.batchQueryFormConfig(formConfigReqDTO);
        return IdmResDTO.success(formConfigRspDTOList);
    }

    /**
     * 根据id集合批量查询自定义配置详情
     */
    @PostMapping(value = "/batchQueryCustomConfigDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<CustomConfigDetailRspDTO>> batchQueryCustomConfigDetails(@RequestBody @Valid CustomConfigDetailReqDTO customConfigDetailReqDTO) {
        List<CustomConfigDetailRspDTO> customConfigDetailRspDTOList = customConfigService.batchQueryCustomConfigDetails(customConfigDetailReqDTO);
        return IdmResDTO.success(customConfigDetailRspDTOList);
    }

    /**
     * 根据id集合批量查询自定义配置详情，并返回对应的名称关系map
     */
    @PostMapping(value = "/batchQueryCustomConfigDetailsForMap", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<Map<Long, String>> batchQueryCustomConfigDetailsForMap(@RequestBody @Valid CustomConfigDetailReqDTO customConfigDetailReqDTO) {
        List<CustomConfigDetailRspDTO> customConfigDetailRspDTOList = customConfigService.batchQueryCustomConfigDetails(customConfigDetailReqDTO);
        return IdmResDTO.success(customConfigDetailRspDTOList.stream().collect(Collectors.toMap(CustomConfigDetailRspDTO::getId, CustomConfigDetailRspDTO::getName)));
    }

    /**
     * 根据条件批量查询自定义配置列表
     */
    @PostMapping(value = "/batchQueryCustomConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<CustomConfigRspDTO>> batchQueryCustomConfigs(@RequestBody @Valid CustomConfigReqDTO customConfigReqDTO) {
        List<CustomConfigRspDTO> customConfigRspDTOS = customConfigService.batchQueryCustomConfigs(customConfigReqDTO);
        return IdmResDTO.success(customConfigRspDTOS);
    }

    /**
     * 根据条件批量查询审核配置列表
     */
    @PostMapping(value = "/lookUpAuditConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<AuditConfigTypeRspDTO>> lookUpAuditConfig(@RequestBody @Valid AuditConfigTypeReqDTO queryDTO) {
        List<AuditConfigTypeRspDTO> auditConfigTypeRspDTOList = auditConfigTypeService.queryForList(queryDTO);
        return IdmResDTO.success(auditConfigTypeRspDTOList);
    }

    /**
     * 根据楼盘id列表查询对应的业主
     */
    @PostMapping(value = "/lookUpUserIdsByBuildingIds", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<Map<Long, List<Long>>> lookUpUserIdsByBuildingIds(@RequestBody @Valid UserHouseAuditBuildingReqDTO reqDTO) {
        return IdmResDTO.success(userHouseAuditService.lookUpUserIdsByBuildingIds(reqDTO.getBuildingIds()));
    }

    /**
     * 批量查询表单配置-常用选项设置数据
     */
    @PostMapping(value = "/batchQueryCommonOptionSetting", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<List<CommonOptionSettingRspDTO>> batchQueryCommonOptionSetting(@RequestBody @Valid CommonOptionSettingReqDTO dto) {
        AssertUtil.isFalse(CollectionUtils.isEmpty(dto.getIdList()),"常用选项设置ID列表不能为空");
        List<CommonOptionSettingEntity> entityList = commonOptionSettingMapper.selectBatchIds(dto.getIdList());
        return IdmResDTO.success(BeanMapper.mapList(entityList, CommonOptionSettingRspDTO.class));
    }

    /**
     * 外部平台参数信息分页查询
     *
     * @return IdmResDTO<IPage>
     * @Param
     */
    @PostMapping("/queryPlatfromInfoPage")
    public IdmResDTO<IPage<PlatfromInfoRespDTO>> queryPlatfromInfoPage(@RequestBody PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryForPage(dto));
    }
}
