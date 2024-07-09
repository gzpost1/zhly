package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;

import java.util.List;

/**
 * <p>
 * 租赁合同 Mapper 接口
 * </p>
 *
 * @author MJ~
 * @since 2024-06-19
 */
public interface TbContractLeaseMapper extends BaseMybatisMapper<TbContractLeaseEntity> {
    List<BaseVO> statisticsContract();
}