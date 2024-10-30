package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.lease.dto.contract.ContractLeaseStatisticParam;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.vo.ContractBoardInfoVo;
import cn.cuiot.dmp.lease.vo.ContractBoardVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MJ~
 * @since 2024-10-22
 */
public interface ContractBoardMapper {
    Integer queryArchiveNum(@Param("companyId") Long companyId);

    List<String> queryArchiveName(@Param("companyId") Long companyId);

    Integer queryHouseNum(@Param("companyId") Long companyId);

    Integer queryLeaseHouseNum(@Param("companyId") Long companyId);

    Integer queryUnLeaseHouseNum(@Param("companyId") Long companyId);

    Integer queryLeaseContractNum(@Param("companyId") Long companyId);

    Integer queryIntentionContractNum(@Param("companyId") Long companyId);

    List<ContractBoardInfoVo> queryLoupanBoardInfo(@Param("companyId") Long companyId);

}
