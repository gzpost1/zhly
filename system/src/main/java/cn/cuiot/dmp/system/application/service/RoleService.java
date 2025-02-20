package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseRoleReqDto;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import java.util.List;
import java.util.Map;

/**
 * @Author xieSH
 * @Description 角色管理业务层接口
 * @Date 2021/8/10 15:59
 * @param
 * @return
 **/
public interface RoleService {

    /**
     * 角色列表分页查询
     *
     * @param paramsMap 参数集合
     * @return
     */
    PageResult<RoleDTO> getRoleListByPage(Map<String, Object> paramsMap);

    /**
     * 角色列表-下拉框
     *
     * @param paramsMap 参数集合
     * @return
     */
    List<RoleDTO> getRoleListNotDefault(Map<String, Object> paramsMap);

    /**
     * 删除（批量删除）角色
     *
     * @param roleBo
     * @return
     */
    int deleteRoles(RoleBo roleBo);


    /**
     * 根据orgId查询角色
     *
     *
     * @param orgId
     * @param userId 账户id
     * @return
     */
    List<RoleDTO> getRoleListByOrgId(String orgId, String userId);

    /**
     * 新增角色
     *
     * @param dto 角色信息
     * @return
     */
    Long createRole(CreateRoleDto dto);

    /**
     * 根据角色id查询角色详细信息
     * @param roleId
     * @param orgId
     * @param userId
     * @return
     */
    RoleDTO getRoleInfo(String roleId, String orgId, String userId);



    /**
     * 修改角色信息
     *
     * @param roleBo 角色信息
     * @return
     */
    Long updateRole(RoleBo roleBo);


    /**
     * 查询角色
     */
    List<BaseRoleDto> lookUpRoleList(BaseRoleReqDto query);

    /**
     * 启停用
     */
    void updateStatus(UpdateStatusParam updateStatusParam, Long sessionUserId, Long sessionOrgId);

}
