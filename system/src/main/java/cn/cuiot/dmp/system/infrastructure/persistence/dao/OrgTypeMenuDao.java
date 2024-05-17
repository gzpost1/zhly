package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeMenuDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业账号类型菜单
 * @author wqd
 * @classname OrgTypeMenuDao
 * @description
 * @date 2022/11/7
 */
@Mapper
public interface OrgTypeMenuDao {

    /**
     * 根据
     * @param orgTypeId
     * @return
     */
    List<String> getMenuIdListByOrgType(@Param("orgTypeId") Long orgTypeId);

    /**
     * 根据类型删除菜单
     * @param orgTypeId
     */
    void deleteMenu(@Param("orgTypeId") Long orgTypeId);

    /**
     * 插入
     * @param list
     */
    void insertMenu(@Param("list") List<OrgTypeMenuDto> list);

}
