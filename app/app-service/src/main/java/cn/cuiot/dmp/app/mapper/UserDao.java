package cn.cuiot.dmp.app.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author jiangze
 * @classname UserDao
 * @description 用户Dao
 * @date 2020-06-23
 */
@Mapper
public interface UserDao {

    /**
     * 获取user对应的pk_org_id
     */
    @Select("select pk_org_id from user_org where pk_user_id = #{pkUserId} order by id limit 1")
    Long getOrgId(@Param("pkUserId") Long pkUserId);

    /**
     * 获取user对应的pk_org_id
     */
    @Select("select pk_user_id from user_org where pk_org_id = #{orgId}")
    List<Long> getUserId(@Param("orgId") String orgId);

    /**
     * 找到用户所对应的角色
     */
    @Select("select pk_role_id from user_role where pk_user_id = #{pkUserId} and pk_org_id = #{pkOrgId} order by id limit 1")
    String getRoleId(@Param("pkUserId") String pkUserId, @Param("pkOrgId") String pkOrgId);

    /**
     * 根据角色id找到角色名称
     */
    @Select("select role_name from role where id = #{0}")
    String getRoleName(String roleId);

    /**
     * 根据角色id找到角色key
     */
    @Select("select role_key from role where id = #{0}")
    String getRoleKey(String roleId);

    /**
     * 找到用户所对应的组织
     */
    @Select("select pk_dept_id from user_org where pk_user_id = #{pkUserId} and pk_org_id = #{pkOrgId} order by id limit 1")
    String getDeptId(@Param("pkUserId") String pkUserId, @Param("pkOrgId") String pkOrgId);

    /**
     * 删除中间表关联关系
     */
    @Delete("delete from user_org where pk_user_id = #{userId} and pk_org_id = #{orgId}")
    int deleteUserOrg(@Param("userId") String userId, @Param("orgId") String orgId);

    /**
     * 删除中间表关联关系
     */
    @Delete("delete from user_org where pk_org_id = #{orgId}")
    int deleteUserOrgByOrgId(@Param("orgId") String orgId);

    /**
     * 添加用户与账户中间表关系数据
     */
    @Insert("INSERT INTO user_org(id,pk_user_id,pk_org_id,pk_dept_id,created_by) VALUES (#{id},#{uid},#{oid},#{did},#{createdBy})")
    int insertUserOrg(@Param("id") Long id, @Param("uid") Long uid, @Param("oid") Long oid,
            @Param("did") String did, @Param("createdBy") String createdBy);

    /**
     * 添加用户授权关系
     */
    @Insert("INSERT INTO user_grant(id,org_id,privileges,to_org_id,to_dept_id, to_user_id) VALUES (#{id},#{orgId},#{privileges},#{toOrgId},#{toDeptId},#{toUserId})")
    int insertUserGrant(@Param("id") Long id, @Param("orgId") Long orgId,
            @Param("privileges") String privileges, @Param("toOrgId") String toOrgId,
            @Param("toDeptId") String toDeptId, @Param("toUserId") String toUserId);

    /**
     * 找到用户所对应的组织
     */
    @Select("select to_dept_id from user_grant where org_id = #{orgId} order by id limit 1")
    String getUserGrantDeptId(@Param("orgId") Long orgId);

    /**
     * 删除用户授权关系
     */
    @Delete("delete from user_grant where org_id = #{orgId}")
    int deleteUserGrant(@Param("orgId") Long orgId);

    /**
     * 用户注册时，添加角色
     */
    @Insert("insert into user_role (id,pk_user_id, pk_org_id, pk_role_id, created_by) values"
            + " (#{id},#{pkUserId}, #{pkOrgId}, #{currentPkRole}, 'system')")
    int insertFeferRole(@Param("id") Long id, @Param("pkUserId") Long pkUserId,
            @Param("pkOrgId") Long pkOrgId, @Param("currentPkRole") Long currentPkRole);

    /**
     * 查询该账户下第一条用户（账户所有者）
     */
    @Select("select org_owner from organization where id = #{id} limit 1")
    Long findOrgOwner(@Param("id") String sessionOrgId);


}
