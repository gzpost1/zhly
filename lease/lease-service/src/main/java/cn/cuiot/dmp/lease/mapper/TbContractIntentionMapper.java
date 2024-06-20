package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 意向合同 Mapper 接口
 * </p>
 *
 * @author MJ~
 * @since 2024-06-12
 */
public interface TbContractIntentionMapper extends BaseMybatisMapper<TbContractIntentionEntity> {
    List<Long>  queryContractIdsByHouseName(@Param("name") String name);
}
