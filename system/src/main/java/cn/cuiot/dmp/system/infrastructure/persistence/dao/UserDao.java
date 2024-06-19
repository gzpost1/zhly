package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.UserLabelDto;
import java.time.LocalDateTime;
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
     *
     * @param pkUserId
     * @return
     */
    @Select("select pk_org_id from user_org where pk_user_id = #{pkUserId} order by id limit 1")
    Long getOrgId(@Param("pkUserId") Long pkUserId);

    /**
     * 获取pk_org_id对应的用户ID
     *
     * @param orgId
     * @return
     */
    @Select("select pk_user_id from user_org where pk_org_id = #{orgId}")
    List<Long> getUserId(@Param("orgId") String orgId);

    /**
     * 找到用户所对应的角色
     *
     * @param pkUserId
     * @param pkOrgId
     * @return
     */
    @Select("select pk_role_id from user_role where pk_user_id = #{pkUserId} and pk_org_id = #{pkOrgId} order by id limit 1")
    String getRoleId(@Param("pkUserId") String pkUserId, @Param("pkOrgId") String pkOrgId);

    /**
     * 根据角色id找到角色名称
     *
     * @param roleId
     * @return
     */
    @Select("select role_name from role where id = #{0}")
    String getRoleName(String roleId);

    /**
     * 根据角色id找到角色key
     *
     * @param roleId
     * @return
     */
    @Select("select role_key from role where id = #{0}")
    String getRoleKey(String roleId);

    /**
     * 找到用户所对应的组织
     *
     * @param pkUserId
     * @param pkOrgId
     * @return
     */
    @Select("select pk_dept_id from user_org where pk_user_id = #{pkUserId} and pk_org_id = #{pkOrgId} order by id limit 1")
    String getDeptId(@Param("pkUserId") String pkUserId, @Param("pkOrgId") String pkOrgId);

    /**
     * 删除中间表关联关系
     *
     * @param orgId
     * @param userId
     * @return
     */
    @Delete("delete from user_org where pk_user_id = #{userId} and pk_org_id = #{orgId}")
    int deleteUserOrg(@Param("userId") String userId, @Param("orgId") String orgId);

    /**
     * 删除中间表关联关系
     *
     * @param orgId
     * @return
     */
    @Delete("delete from user_org where pk_org_id = #{orgId}")
    int deleteUserOrgByOrgId(@Param("orgId") String orgId);

    /**
     * 添加用户与账户中间表关系数据
     */
    @Insert("INSERT INTO user_org(id,pk_user_id,pk_org_id,pk_dept_id,created_by) VALUES (#{id},#{uid},#{oid},#{did},#{createdBy})")
    int insertUserOrg(@Param("id") Long id,@Param("uid") Long uid, @Param("oid") Long oid, @Param("did") String did, @Param("createdBy") String createdBy);

    /**
     * 添加用户授权关系
     * @param id
     * @param orgId
     * @param privileges
     * @param toOrgId
     * @param toDeptId
     * @param toUserId
     * @return
     */
    @Insert("INSERT INTO user_grant(id,org_id,privileges,to_org_id,to_dept_id, to_user_id) VALUES (#{id},#{orgId},#{privileges},#{toOrgId},#{toDeptId},#{toUserId})")
    int insertUserGrant(@Param("id") Long id, @Param("orgId") Long orgId, @Param("privileges") String privileges, @Param("toOrgId") String toOrgId, @Param("toDeptId") String toDeptId, @Param("toUserId") String toUserId);

    /**
     * 找到用户所对应的组织
     *
     * @param orgId
     * @return
     */
    @Select("select to_dept_id from user_grant where org_id = #{orgId} order by id limit 1")
    String getUserGrantDeptId(@Param("orgId") Long orgId);

    /**
     * 删除用户授权关系
     *
     * @param orgId
     * @return
     */
    @Delete("delete from user_grant where org_id = #{orgId}")
    int deleteUserGrant(@Param("orgId") Long orgId);

    /**
     * 用户注册时，添加角色
     */
    @Insert("insert into user_role (id,pk_user_id, pk_org_id, pk_role_id, created_by) values" + " (#{id},#{pkUserId}, #{pkOrgId}, #{currentPkRole}, 'system')")
    int insertFeferRole(@Param("id") Long id,@Param("pkUserId") Long pkUserId, @Param("pkOrgId") Long pkOrgId, @Param("currentPkRole") Long currentPkRole);

    /**
     * 查询该账户下第一条用户（账户所有者）
     *
     * @param sessionOrgId
     * @return
     */
    @Select("select org_owner from organization where id = #{id} limit 1")
    Long findOrgOwner(@Param("id") String sessionOrgId);

    /**
     * 删除中间表关联关系
     *
     * @param orgId
     * @param userId
     * @return
     */
    @Delete("delete from user_role where pk_user_id = #{userId} and pk_org_id = #{orgId}")
    int deleteUserRole(@Param("userId") String userId, @Param("orgId") String orgId);

    /**
     * 添加中间关系表
     *
     * @param userId
     * @param roleId
     * @param orgId
     * @param now
     * @param uid
     * @return
     */
    @Insert("insert into user_role(id,pk_user_id, pk_role_id, pk_org_id, created_on, created_by) " + "values (#{id},#{pkUserId}, #{pkRoleId}, #{pkOrgId}, #{createdOn}, #{createdBy})")
    int insertUserRole(@Param("id") Long id, @Param("pkUserId") Long userId, @Param("pkRoleId") Long roleId, @Param("pkOrgId") String orgId, @Param("createdOn") LocalDateTime now, @Param("createdBy") String uid);

    /**
     * 通过角色id获取角色key
     *
     * @param roleId
     * @return
     */
    @Select("SELECT role_key FROM role WHERE id = #{roleId}")
    String findRoleKey(String roleId);

    /**
     * 根据userId查询用户标签信息
     *
     * @param userId 用户id
     * @return
     */
    @Select("select ul.label_id , ul.label_name as otherLabelName, lt.label_name from user_label ul left join label_type lt on ul.label_id = lt.id where ul.user_id = #{userId}")
    UserLabelDto getUserLabelById(@Param("userId") String userId);

    /**
     * 根据组织树查询用户数
     *
     * @param path 组织树
     * @return
     */
    @Select("select count(*) from user u left join user_org uo on u.id = uo.pk_user_id left join department d on uo.pk_dept_id = d.id where d.path like CONCAT(#{path},'%') and u.deleted_flag = 0 and u.user_type = 1")
    Integer getUserCount(@Param("path") String path);


}
