package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.application.param.dto.DepartmentUserDto;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LabelTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserLabelDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author sxh
 * @classname UserDataDao
 * @description 用户Dao
 * @date 2022-11-23
 */
@Mapper
public interface UserDataDao {
    /**
     * 根据条件获取user列表
     *
     * @param params
     * @return
     */
    List<UserDataEntity> searchList(Map<String, Object> params);

    /**
     * 根据条件获取user列表
     *
     * @param params
     * @return
     */
    List<UserDataEntity> searchListByRole(Map<String, Object> params);

    /**
     * 删除用户后，把用户与当前登录的账号的关联关系删除
     *
     * @param ids
     * @param sessionOrgId
     * @return
     */
    int deleteUserOrgByUserPks(@Param("ids") List<Long> ids, @Param("orgId") String sessionOrgId);

    /**
     * 删除用户后，删除与角色的关联
     *
     * @param ids
     * @param sessionOrgId
     * @return
     */
    int deleteUserRoleByUserPks(@Param("ids") List<Long> ids, @Param("orgId") String sessionOrgId);

    /**
     * 查找组织下是否挂了用户
     *
     * @param deptId
     * @return
     */
    int countUserOrgByDeptId(Long deptId);

    /**
     * 获取标签类型列表
     * @param labelType
     * @return
     */
    List<LabelTypeDto> getLabelTypeList(@Param("labelType")String labelType);

    /**
     * 新增用户标签信息
     *
     * @param userLabelDto
     * @return
     */
    int insertUserLabel(UserLabelDto userLabelDto);

    /**
     * 根据用户标签名称查询用户标签id
     *
     * @param labelName
     * @return
     */
    Integer selectLabelIdByName(@Param("labelName")String  labelName);


    /**
     * 根据账户标签名称和账户标签名称查询用户标签id
     *
     * @param labelId
     * @return
     */
    String selectLabelNameByOrgId(@Param("labelId")Integer  labelId);

    /**
     * 检查dept下有无用户或人员
     * @param id
     * @return
     */
    String checkUserInDeptId(@Param("id") Long id);

    /**
     * 检查组织下用户
     * @param orgId
     * @param path
     * @return
     */
    List<DepartmentUserDto> getDepartmentUser(@Param("orgId") Long orgId, @Param("path") String path);

    /**
     * 查找组织下所有用户
     *
     * @param deptId 部门id
     */
    List<UserDataEntity> getUserByDeptId(@Param("deptId") Long deptId);
}
