package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.lease.entity.TbContractBindInfoEntity;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 意向合同关联信息 Mapper 接口
 * </p>
 *
 * @author MJ~
 * @since 2024-06-12
 */
public interface TbContractBindInfoMapper extends BaseMybatisMapper<TbContractBindInfoEntity> {
      List<ContractStatus> queryConctactStatusByHouseIds(@Param("ids") List<Long> ids);
      List<Long>  queryContractIdsByHouseName(@Param("name") String name);
}
