package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgMenuDto;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wqd
 * @classname OrgMenuDao
 * @description
 * @date 2022/11/7
 */
@Mapper
public interface OrgMenuDao {

    /**
     * 插入orgMenu表
     */
    void insertOrgMenu(@Param("list") List<OrgMenuDto> list);

    /**
     * 删除orgMenDao
     * @param orgId
     */
    void deleteByOrgId(@Param("orgId") Long orgId);

    /**
     * 查询菜单list
     * @param orgId
     * @return
     */
    List<String> getMenuListByOrgId(@Param("orgId") String orgId);

    /**
     * 获得org允许的菜单权限
     * @param orgId
     * @return
     */
    List<String> getAllowMenuIdList(@Param("orgId") String orgId);

}
