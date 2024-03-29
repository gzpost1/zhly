package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.SysParamEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author wen
 * @Description SysParamDao
 * @Date 2021/12/28 15:08
 * @param
 * @return
 **/
@Mapper
public interface SysParamDao {

    /**
     * 获取系统参数
     * @param path 组织层级树
     * @return
     */
    @Select("select id, org_Id, title, logo_id, created_by, created_on, dept_tree_path, updater_path" +
            " from sys_param where dept_tree_path = #{path}")
    SysParamEntity getByOrgId(String path);

    /**
     * 保存系统参数
     *
     * @param dto
     * @return
     */
    @Insert("insert into sys_param (org_id, title, dept_tree_path, logo_id, created_by, updater_path) values" +
            " (#{orgId}, #{title}, #{deptTreePath}, #{logoId}, #{userId}, #{updaterPath})")
    int insert(SysParamDto dto);

    /**
     * 更新系统参数
     *
     * @param title
     * @param path
     * @param logoId
     * @param userId
     * @return
     */
    @Update("update sys_param set title=#{title}, logo_id=#{logoId}, updated_by=#{userId}, " +
            " updated_on=now() where dept_tree_path=#{path}")
    int update(@Param("title") String title,
               @Param("path") String path,
               @Param("logoId") Integer logoId,
               @Param("userId") Long userId);

    /**
     * 根据组织获取系统参数列表
     *
     * @param orgPath
     * @return
     */
    @Select("select id, org_id, logo_id, title, created_on, created_by, updated_on, updated_by, dept_tree_path, updater_path" +
            " from sys_param where dept_tree_path like CONCAT(#{path}, '%')")
    List<SysParamEntity> getByPathAll(String orgPath);

    /**
     * 根据组织删除组织及下级配置
     * @param deptTreePath
     * @return
     */
    @Delete("delete from sys_param where dept_tree_path like CONCAT(#{path}, '%')")
    int deleteAll(String deptTreePath);

    /**
     * 删除本级组织配置
     *
     * @param deptTreePath
     * @return
     */
    @Delete("delete from sys_param where dept_tree_path = #{path}")
    int deleteByPath(String deptTreePath);

    /**
     * 查询组织下配置数量
     *
     * @param path
     * @return
     */
    @Select("select count(*) from sys_param where dept_tree_path like CONCAT(#{path}, '-%') " +
            "or dept_tree_path like CONCAT(#{path},'_%')")
    int getSysParamWhether(String path);
}
