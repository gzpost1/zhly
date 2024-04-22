package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeResDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jz
 * @classname TreeDao
 * @description 树管理
 * @date 2023/04/10
 */
@Mapper
public interface TreeDao {

    /**
     * 子组织树列表查询
     * @param dGroup 组织类型
     * @param deptId 组织ID
     * @param deptName 组织名称（模糊查询）
     * @param deptTreePath 组织树
     * @param orgId 租户id
     * @param orgTypeList 账户类型（5：联通账户，11：物业账户，12：通用账户）
     * @param orgLabelList 账户标签列表（9：厂园区，13：联通管理方）
     * @return
     */
    List<DeptTreeResDto> getDeptChildList(@Param("dGroup") String dGroup,
                                          @Param("init") Boolean init,
                                          @Param("deptId") String deptId,
                                          @Param("deptName") String deptName,
                                          @Param("deptTreePath") String deptTreePath,
                                          @Param("orgId") String orgId,
                                          @Param("orgTypeList") List<Integer> orgTypeList,
                                          @Param("orgLabelList") List<Integer> orgLabelList);


    /**
     * 根据组织树查询组织详情
     * @param rootDeptTreePath 根组织树
     * @param childDeptTreePathList 子组织树列表
     * @return
     */
    List<DeptTreeResDto> getDeptList(@Param("rootDeptTreePath") String rootDeptTreePath,
                                     @Param("childDeptTreePathList") List<String> childDeptTreePathList);

}
