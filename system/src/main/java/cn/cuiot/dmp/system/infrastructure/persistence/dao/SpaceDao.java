package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author sxh
 * @classname SpaceDao
 * @date 2022-11-23
 */
@Mapper
public interface SpaceDao {

    /**
     * 查询列表
     * @param path
     * @param floorName
     * @param labelId
     * @param group
     * @return
     */
    List<DepartmentDto> getDeptListByPathAndGroupFloor(@Param("path") String path,@Param("floorName") String floorName, @Param("labelId") Integer labelId, @Param("group") Integer group);

    /**
     * 获得根节点
     *
     * @param orgId
     * @param id
     * @return
     */
    List<GetDepartmentTreeLazyResDto> getRootDepartmentLazy(@Param("orgId") String orgId, @Param("id") String id);

    /**
     * 查询子节点
     * @param parentId
     * @param dGroupList
     * @return
     */
    List<GetDepartmentTreeLazyResDto> getUserDepartmentLazy(@Param("parentId") Long parentId, @Param("dGroupList") List<Integer> dGroupList);

    /**
     * 查询子节点
     * @param parentId
     * @param dGroupList
     * @return
     */
    List<GetDepartmentTreeLazyResDto> getUserDepartmentLazyChange(@Param("parentId") Long parentId, @Param("dGroupList") List<Integer> dGroupList);

    /**
     * 根据path，组织名（模糊），dGroup属性查询组织
     *
     * @param path
     * @param departmentName
     * @param dGroupList
     * @return
     */
    List<GetDepartmentTreeLazyByNameResDto> getDepartmentByNameAndPathAndDepartmentGroup(@Param("path") String path, @Param("departmentName") String departmentName, @Param("dGroupList") List<String> dGroupList);

    /**
     * 根据parentId获得子组织List
     * @param parentId
     * @return
     */
    TreeSet<GetDepartmentTreeLazyByNameResDto> getChildDepartmentListByParentId(@Param("parentId") String parentId);

    /**
     * 根据id获得当前组织
     * @param parentId
     * @return
     */
    @MapKey("id")
    Map<Long, GetDepartmentTreeLazyByNameResDto> getParentDepartmentByParentId(@Param("parentId") String parentId);

    /**
     * 查询组织下最大单员数
     *
     * @param path
     * @return
     */
    int getMaxUnitByPath(@Param("path") String path);

}

