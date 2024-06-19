package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;

/**
 * <p>
 * 意向金 Mapper 接口
 * </p>
 *
 * @author MJ~
 * @since 2024-06-12
 */
public interface TbContractIntentionMoneyMapper extends BaseMybatisMapper<TbContractIntentionMoneyEntity> {
    void clearMoney();
}
