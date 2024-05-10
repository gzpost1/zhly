package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertSonDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;
import java.util.List;

/**
 * @param
 * @Author xieSH
 * @Description 组织管理Service
 * @Date 2021/8/17 9:44
 * @return
 **/
public interface DepartmentService {

    /**
     * 新增子组织
     * @param dto
     * @return
     * @Author xieSH
     * @Description 新增子组织
     * @Date 2021/8/18 11:18
     **/
    Long insertSonDepartment(InsertSonDepartmentDto dto);

    /**
     * 新增组织属性
     * @param dto
     * @return
     * @Author wen
     * @Description 新增组织属性
     * @Date 2021/8/17 9:53
     **/
    int insertDepartmentProperty(DepartmentPropertyDto dto);

    /**
     * 修改组织属性
     * @param dto
     * @return
     * @Author wen
     * @Description 修改组织属性
     * @Date 2021/8/17 9:53
     **/
    int updateDepartmentProperty(DepartmentPropertyDto dto);

    /**
     * 查询组织属性
     * @param deptId
     * @param key
     * @return
     * @Author wen
     * @Description 查询组织属性
     * @Date 2021/8/17 9:53
     **/
    String getDepartmentProperty(Long deptId, String key);

    /**
     * 修改组织
     * @param dto
     * @return
     * @Author xieSH
     * @Description 修改组织
     * @Date 2021/8/17 15:19
     **/
    int updateDepartment(UpdateDepartmentDto dto);

    /**
     * 查询
     * @param orgId
     * @return
     * @Author xieSH
     * @Description 查询
     * @Date 2021/8/17 15:19
     **/
    List<DepartmentEntity> getDeptRootByOrgId(String orgId);

    /**
     * 根据组织id查询组织名称
     * @param id 组织id
     * @return
     * @Author xieSH
     * @Description 根据组织id查询组织名称
     * @Date 2021/8/17 15:19
     **/
    DepartmentEntity getDeptById(String id);

    /**
     * 查询组织树
     * @param orgId
     * @param userId
     * @param type
     * @return
     * @Author xieSH
     * @Description 查询组织树
     * @Date 2021/8/18 17:06
     **/
    List<DepartmentTreeVO> getDepartmentTree(String orgId, String userId, String type);

    /**
     * 删除组织
     * @param updateDepartmentDto
     * @return
     * @Author xieSH
     * @Description 删除组织
     * @Date 2021/8/25 11:18
     **/
    void deleteDepartment(UpdateDepartmentDto updateDepartmentDto);

    /**
     * 查询当前组织的子组织列表（包括当前组织）
     *
     * @param orgId  String
     * @param deptId Long
     * @return List<Long>
     */
    List<Long> getChildrenDepartmentIds(String orgId, Long deptId);


    /**
     * 批量删除组织
     * @param ids
     * @param sessionOrgId
     */
    void batchDeleteDepartment(List<Long> ids, String sessionOrgId);

    /**
     * 用户组织树搜索
     *
     * @param dto
     * @return
     */
    GetDepartmentTreeLazyByNameResDto getUserDepartmentTreeLazyByName(GetDepartmentTreeLazyByNameReqDto dto);

    /**
     * 根据deptId查询组织路径
     * @param deptId
     * @return
     */
    String getDeptPathById(String deptId);

    /**
     * 根据用户id查询用户组织path
     *
     * @param pkUserId
     * @return
     */
    DepartmentDto getPathByUser(Long pkUserId);

    /**
     * 空间树（全路径到到小区/区域级）
     *
     * @param orgId
     * @param userId
     * @return
     */
    List<DepartmentTreeVO> getAllSpaceTree(String orgId, String userId);

    /**
     * 组织管理 组织树懒加载
     * @param getDepartmentTreeLazyReqDto
     * @return
     */
    List<GetDepartmentTreeLazyResDto> manageGetDepartmentTreeLazy(GetDepartmentTreeLazyReqDto getDepartmentTreeLazyReqDto);

    /**
     * 查询部门
     */
    List<DepartmentDto> lookUpDepartmentList(DepartmentReqDto query);

}
