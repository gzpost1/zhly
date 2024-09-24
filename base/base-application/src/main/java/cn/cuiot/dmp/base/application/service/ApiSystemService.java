package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.*;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;

import java.util.List;
import java.util.Map;

/**
 * ApiSystemService
 *
 * @author: wuyongchong
 * @date: 2024/4/25 15:52
 */
public interface ApiSystemService {

    /**
     * 获取权限菜单
     */
    List<CommonMenuDto> getPermissionMenus(String orgId,String userId);

    /**
     * 查询角色
     */
    List<BaseRoleDto> lookUpRoleList(BaseRoleReqDto query);

    /**
     * 查询部门
     */
    List<DepartmentDto> lookUpDepartmentList(DepartmentReqDto query);

    /**
     * 查询用户
     */
    List<BaseUserDto> lookUpUserList(BaseUserReqDto query);

    /**
     * 获取用户信息
     */
    BaseUserDto lookUpUserInfo(BaseUserReqDto query);

    /**
     * 获取部门信息
     */
    DepartmentDto lookUpDepartmentInfo(Long deptId, Long userId, Long orgId);

    /**
     * 查询子部门
     */
    List<DepartmentDto> lookUpDepartmentChildList(DepartmentReqDto query);

    /**
     * 查询组织树
     */
    List<DepartmentTreeRspDTO> lookUpDepartmentTree(Long orgId, Long userId);

    /**
     * 根据业务类型id列表获取业务类型列表（流程/工单配置）
     */
    List<BusinessTypeRspDTO> batchGetBusinessType(BusinessTypeReqDTO businessTypeReqDTO);

    /**
     * 通过名称查询表单配置详情
     */
    FormConfigRspDTO lookUpFormConfigByName(FormConfigReqDTO formConfigReqDTO);

    /**
     * 批量查询表单配置
     */
    List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO);

    /**
     * 根据id集合批量查询自定义配置详情
     */
    List<CustomConfigDetailRspDTO> batchQueryCustomConfigDetails(CustomConfigDetailReqDTO customConfigDetailReqDTO);

    /**
     * 根据id集合批量查询自定义配置详情，并返回对应的名称关系map
     */
    Map<Long, String> batchQueryCustomConfigDetailsForMap(CustomConfigDetailReqDTO customConfigDetailReqDTO);

    /**
     * 根据条件批量查询自定义配置列表
     */
    List<CustomConfigRspDTO> batchQueryCustomConfigs(CustomConfigReqDTO customConfigReqDTO);

    /**
     * 根据条件批量查询审核配置列表
     */
    List<AuditConfigTypeRspDTO> lookUpAuditConfig(AuditConfigTypeReqDTO queryDTO);

    /**
     * 根据楼盘id列表查询对应的业主
     */
    Map<Long, List<Long>> lookUpUserIdsByBuildingIds(UserHouseAuditBuildingReqDTO reqDTO);

    /**
     * 根据企业id查询企业名称列表
     */
    List<OrganizationRespDTO> queryOrganizationList(List<Long> companyIds);
}
