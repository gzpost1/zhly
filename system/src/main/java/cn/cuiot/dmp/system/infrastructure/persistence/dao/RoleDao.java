package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.RoleEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AddMenuDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author xieSH
 * @Description RoleDao
 * @Date 2021/8/10 16:02
 * @param
 * @return
 **/
@Mapper
public interface RoleDao {

    /**
     * 角色列表分页查询
     *
     * @param paramsMap
     * @return
     */
    List<RoleDTO> selectRoleListByPage(Map<String, Object> paramsMap);

    /**
     * 角色列表分页查询
     *
     * @param paramsMap
     * @return
     */
    List<RoleDTO> selectRoleList(Map<String, Object> paramsMap);

    /**
     * 查询角色关联的用户
     *
     * @param orgId 账号id
     * @param ids   roleID集合
     * @return
     */
    List<Map<String, Long>> selectUserIdsByRoleIds(@Param("orgId") String orgId, @Param("idList") List<Long> ids);

    /**
     * 插入角色
     *
     * @param entity 角色信息
     * @return
     */
    int insertRole(RoleEntity entity);

    /**
     * 修改角色
     *
     * @param roleBo 角色信息
     * @return
     */
    int updateRole(RoleBo roleBo);

    /**
     * 新建角色
     *
     * @param roleEntity
     * @return
     */
    int insert(RoleEntity roleEntity);

    /**
     * 更新角色
     *
     * @param updateParams
     * @return
     */
    int update(RoleEntity updateParams);

    /**
     * 删除角色和菜单的关联关系
     * @param roleId pkRoleId
     * @return
     */
    int deleteMenuRole(@Param("roleId") Long roleId);

    /**
     * 根据id删除角色
     *
     * @param orgId         账户id
     * @param ids           角色自增ID集合
     * @param deleteTime   角色删除时间
     * @return
     */
    int deleteRolesByIds(@Param("orgId") String orgId, @Param("idList") List<Long> ids, @Param("deleteTime")LocalDateTime deleteTime);


    /**
     * 根据pk_id查询角色信息
     *
     * @param idList
     * @return
     */
    List<RoleEntity> selectRoleByRoleIds(List<Long> idList);

    /**
     * 批量删除菜单与角色的关联关系
     *
     * @param orgId
     * @param idList
     */
    void deleteBatchMenuRole(@Param("orgId") String orgId, @Param("idList") List<Long> idList);

    /**
     * 根据orgId查询角色
     *
     * @param orgId 账号id
     * @return
     */
    List<RoleDTO> selectRoleListByOrgId(@Param("orgId") String orgId);


    /**
     * 根据角色id查询角色信息
     *
     * @param roleId
     * @return
     */
    RoleEntity getOneRole(String roleId);

    /**
     * 查询数据库默认角色对应菜单
     *
     * @param rolePk
     * @return
     */
    List<String> findMenuPksByRolePk(@Param("rolePk") Long rolePk);

    /**
     * 批量插入角色与菜单的关联
     *
     * @param currentPkRole
     * @param menuIds
     * @param createdBy
     * @param createdByType
     * @return
     */
    int insertMenuRole(@Param("roleId") Long currentPkRole, @Param("menuIds") List<String> menuIds,
        @Param("createdBy") String createdBy, @Param("createdByType") Integer createdByType);


    /**
     * 插入账户和角色的关系
     *
     * @param paramsMap 参数map
     * @return
     */
    int insertOrgRole(Map<String, Object> paramsMap);

    /**
     * 删除账户下的角色
     *
     * @param orgId 账号id
     * @param ids   角色id集合
     * @return
     */
    int deleteOrgRole(@Param("orgId") String orgId, @Param("roleIdList") List<Long> ids);

    /**
     * 根据id查询角色信息
     *
     * @param orgId 账号id
     * @param userId    角色id
     * @return
     */
    RoleDTO selectRoleByUserId(@Param("orgId") Long orgId, @Param("userId") Long userId);

    /**
     * 根据账户id和角色id查询角色信息
     * @param orgId
     * @param roleId
     * @param userId
     * @return
     */
    RoleDTO selectOneRole(@Param("orgId") Long orgId, @Param("roleId") Long roleId, @Param("userId") Long userId);


    /**
     * 根据角色id查询菜单id和菜单类型集合
     *
     * @param roleId
     * @return
     */
    List<AddMenuDto> getRoleMenu(Integer roleId);



    /**
     * 根据id查询角色信息
     *
     * @param orgId 账号id
     * @param id    角色自增id
     * @return
     */
    RoleDTO selectRoleById(@Param("orgId") Long orgId, @Param("id") Long id);

    /**
     * 添加菜单与角色关联关系
     *
     * @param roleBo
     */
    void insertMenusRole(RoleBo roleBo);


    /**
     * 根据角色名查询角色信息
     */
    RoleDTO selectRoleByName(@Param("roleName") String roleName,@Param("orgId") Long orgId);

}
