package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.lease.dto.contract.ContractLeaseStatisticParam;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import org.apache.ibatis.annotations.Param;

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
    List<BaseVO> statisticsContract(@Param("orgId") Long orgId);

    Long queryLeaseStatistic(@Param("params") ContractLeaseStatisticParam params);
}
