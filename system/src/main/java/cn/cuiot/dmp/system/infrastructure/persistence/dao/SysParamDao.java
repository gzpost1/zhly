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
     * @param orgId
     * @return
     */
    @Select("select id, org_id, title, logo_path, created_by, created_on" +
            " from sys_param where org_id = #{orgId}")
    SysParamEntity getByOrgId(String orgId);

    /**
     * 保存系统参数
     *
     * @param dto
     * @return
     */
    @Insert("insert into sys_param (org_id, title,logo_path, created_by) values" +
            " (#{sessionOrgId}, #{title}, #{logoPath}, #{sessionUserId})")
    int insert(SysParamDto dto);

    /**
     * 更新系统参数
     *
     * @param dto
     * @return
     */
    @Update("update sys_param set title=#{title}, logo_path=#{logoPath}, updated_by=#{sessionUserId}, " +
            " updated_on=now() where org_id=#{sessionOrgId}")
    int update(SysParamDto dto);
}
