package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wqd
 * @classname OrgTypeMenuRootDao
 * @description
 * @date 2022/11/7
 */
@Mapper
public interface OrgTypeMenuRootDao {

    /**
     * 根据
     * @param orgTypeId
     * @return
     */
    List<String> getMenuIdListByOrgType(@Param("orgTypeId") Integer orgTypeId);
}
