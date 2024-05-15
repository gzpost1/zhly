package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.BusinessTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeMapper extends BaseMapper<BusinessTypeEntity> {

    int countFlowConfigByBusinessType(@Param("businessTypeId") Long businessTypeId);

    int countFlowTaskConfigByBusinessType(@Param("businessTypeId") Long businessTypeId);

}
