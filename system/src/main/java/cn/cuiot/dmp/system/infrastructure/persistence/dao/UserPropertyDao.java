package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.UserPropertyEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wqd
 * @classname UserProperty
 * @description
 * @date 2022/8/8
 */
@Mapper
public interface UserPropertyDao {

    /**
     * 根据条件获得单个属性
     * @param orgId
     * @param userId
     * @param propertyKey
     * @param val
     * @return
     */
    List<UserPropertyEntity> getUserProperty(@Param("orgId") String orgId, @Param("userId") Long userId, @Param("propertyKey") String propertyKey, @Param("val") String val);

}
