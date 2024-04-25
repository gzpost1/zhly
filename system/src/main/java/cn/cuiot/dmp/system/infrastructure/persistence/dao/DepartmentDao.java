package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeResDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @param
 * @Author xieSH
 * @Description DepartmentDao
 * @Date 2021/8/17 10:21
 * @return
 **/
@Mapper
public interface DepartmentDao {

    /**
     * 新增
     * @param entity
     * @return
     */
    Long insertDepartment(DepartmentEntity entity);

    /**
     * 修改
     * @param entity
     * @return
     */
    int updateDepartment(DepartmentEntity entity);

    /**
     * 主键查询
     * @param id
     * @return
     */
    DepartmentEntity selectByPrimary(Long id);

    /**
     * 通过house主键id获取房屋path
     * @param id
     * @return
     */
    String getHousePathByHousePrimaryKey(Long id);

    /**
     * 通过房屋id列表批量获取房屋path
     * @param idList 房屋id列表
     * @return
     */
    List<String> getHousePathListByIdList(@Param("idList") List<Long> idList);

    /**
     * 通过房屋id列表批量获取组织id
     * @param idList 房屋id列表
     * @return
     */
    List<Long> getDeptIdListByHouseIdList(@Param("idList") List<Long> idList);

    /**
     *  根据path查询最高小区层级的id
     * @param path
     * @return
     */
    DepartmentEntity getCommunityIdByPath(@Param("path") String path);

    /**
     * 查询组织list
     * @param orgId
     * @return
     */
    List<DepartmentEntity> selectByOrgId(String orgId);

    /**
     * 查询根组织
     * @param orgId
     * @return
     */
    List<DepartmentEntity> selectRootByOrgId(String orgId);

    /**
     * 根据条件查询组织名称数量
     * @param departmentName
     * @param orgId
     * @return
     */
    int countByDepartmentName(@Param("departmentName") String departmentName, @Param("orgId") Long orgId);

    /**
     * 查询
     * @param departmentName
     * @param orgId
     * @param id
     * @return
     */
    int countByDepartmentNameForUpdate(@Param("departmentName") String departmentName,@Param("orgId")  Long orgId,@Param("id")  Long id);

    /**
     * 根据条件查询
     * @param departmentName
     * @param parentId
     * @return
     */
    @Select("select 1 from department where department_name = #{departmentName} and parent_id = #{parentId} limit 1")
    Integer selectByNameAndPatentId(@Param("departmentName") String departmentName,@Param("parentId") Long parentId);

    /**
     * 根据条件查询
     * @param currentFloor
     * @param parentId
     * @return
     */
    @Select("select 1 from department d left join department_property dp on d.id = dp.dept_id \n" +
            "where dp.property_key = 'current_floor' and dp.val = #{currentFloor}  and d.parent_id = #{parentId} limit 1")
    Integer selectByPropertyAndPatentId(@Param("currentFloor") String currentFloor,@Param("parentId") Long parentId);

    /**
     * 根据条件查询
     * @param currentFloor
     * @param parentId
     * @param id
     * @return
     */
    @Select("select 1 from department d left join department_property dp on d.id = dp.dept_id \n" +
            "where dp.property_key = 'current_floor' and dp.val = #{currentFloor} and d.parent_id = #{parentId} and d.id != #{id} limit 1")
    Integer selectByPropertyAndPatentIdForUpdate(@Param("currentFloor") String currentFloor,@Param("parentId")  Long parentId,@Param("id")  Long id);

    /**
     * 根据条件查询组织
     * @param departmentName
     * @param parentId
     * @param id
     * @return
     */
    @Select("select 1 from department where department_name = #{departmentName} and parent_id = #{parentId} and id != #{id} limit 1")
    Integer selectByNameAndPatentIdForUpdate(@Param("departmentName") String departmentName, @Param("parentId") Long parentId,@Param("id")  Long id);

    /**
     * 根据parentId查询
     * @param parentId
     * @return
     */
    int countByParentId(Long parentId);

    /**
     * 根据父级id列表查询
     * @param parentIds
     * @return
     */
    int countByParentIds(@Param("parentIds") List<Long> parentIds);

    /**
     * 根据主键id删除
     * @param id
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 根据主键id删除
     * @param ids
     * @param orgId
     */
    void batchDelete(@Param("ids") List<Long> ids,@Param("orgId") String orgId);

    /**
     * 根据主键id删除
     * @param ids
     */
    void batchDeleteProperty(@Param("ids") List<Long> ids);

    /**
     * 根据parentId查询组织
     * @param parentId
     * @param path
     * @return
     */
    List<DepartmentEntity> getDepartmentListByParentIdAndPath(@Param("parentId")Long parentId, @Param("path")String path);

    /**
     * 根据组织id查询下级组织
     *
     * @param spaceId 组织id
     * @return
     */
    List<SpaceTreeResDto> getSpaceListByParentId(@Param("spaceId")Long spaceId, @Param("dGroup")Integer dGroup,
                                                 @Param("path")String reqPath);

    /**
     * 获取部门列表
     * @param ids
     * @return
     */
    List<DepartmentEntity> getDepartmentList(@Param("ids") List<Long> ids);

    /**
     * 查询最大sort
     * @param parentId
     * @return
     */
    Integer getMaxSortByParentId(Long parentId);

    /**
     * 添加组织属性数据
     *
     * @param did Long
     * @param key String
     * @param val String
     * @return int
     */
    @Insert("INSERT INTO department_property(dept_id,property_key,val) VALUES (#{did},#{key},#{val})")
    int insertDepartmentProperty(@Param("did") Long did, @Param("key") String key, @Param("val") String val);

    /**
     * 修改组织属性数据
     *
     * @param did Long
     * @param key String
     * @param val String
     * @return int
     */
    @Update("update department_property set val=#{val} where dept_id = #{did} and property_key=#{key}")
    int updateDepartmentProperty(@Param("did") Long did, @Param("key") String key, @Param("val") String val);

    /**
     * 查询组织属性数据
     *
     * @param did Long
     * @param key String
     * @return int
     */
    @Select("select val from department_property where dept_id = #{did} and property_key=#{key} limit 1")
    String getDepartmentProperty(@Param("did") Long did, @Param("key") String key);


    /**
     * 根据组织path查询组织名称
     *
     * @param treePath
     * @return
     */
    @Select("SELECT d.department_name,d.id,d.d_group,d.pk_org_id,o.org_name,d.path FROM department d,organization o where o.id=d.pk_org_id and path = #{treePath} LIMIT 1")
    DepartmentEntity getDeptNameByPath(String treePath);


    /**
     * 用户id查组织path
     *
     * @param userId
     * @return
     */
    DepartmentDto getPathByUser(String userId);

    /**
     * 查询组织完整路径
     *
     * @param tenantDept
     * @return
     */
    String getFullPathById(String tenantDept);


    /**
     * 统一账户下组织名不能相同
     * @param pkOrgId
     * @param departmentName
     * @return
     */
    String selectDepartmentName(@Param("pkOrgId") Long pkOrgId, @Param("departmentName") String departmentName);

    /**
     * 根据id查询组织路径
     * @param deptId
     * @return
     */
    @Select("select path from department where id = #{deptId}")
    String getDeptPathById(String deptId);

    /**
     * 查询入参组织所处的系统组织
     *
     * @param path
     * @return
     */
    DepartmentEntity getPathBySpacePath(@Param("path") String path);


    /**
     * 获取小区个数
     * @param path
     * @return
     */
    int getCommunityByPath(@Param("path")String path);

    /**
     * 通过id修改dept 名称
     * @param departmentName
     * @param id
     * @return
     */
    Integer updateDepartmentNameById(@Param("departmentName")String departmentName , @Param("id") Long id);

    /**
     * 通过id获取dept信息
     * @param id
     * @return
     */
    DepartmentEntity getDepartmentById(@Param("id") String id);

    /**
     * 空间树（全路径到到小区/区域级）
     *
     * @param path
     * @param orgId
     * @return
     */
    List<DepartmentEntity> getAllSpaceTree(@Param("path") String path, @Param("orgId") String orgId);

    /**
     * 获得用户所在组织得dGroup属性
     * @param orgId
     * @param userId
     * @return
     */
    Map<String, String> getDgroupByUserIdAndOrgId(@Param("orgId") String orgId, @Param("userId") String userId);


    /**
     * 按组织ID获取路径
     *
     * @param orgId
     * @return
     */
    String getPathByOrgId(@Param("orgId") String orgId);

    /**
     * 批量插入组织属性表
     * @param departmentPropertyDtoList
     */
    void insertDepartmentPropertyBatch(@Param("departmentPropertyDtoList") List<DepartmentPropertyDto> departmentPropertyDtoList);


    /**
     *  根据path查询最高小区层级的path
     * @param path
     * @return
     */
    String getCommunityPathByPath(@Param("path") String path);

    /**
     * 校验deptIdList是否为当前用户下的
     * @param orgId 租户id
     * @param userId 用户id
     * @param deptIdList 组织id
     * @return
     */
    List<Long> checkDeptIdList(@Param("orgId") String orgId, @Param("userId") String userId, @Param("deptIdList") List<Long> deptIdList);


}
