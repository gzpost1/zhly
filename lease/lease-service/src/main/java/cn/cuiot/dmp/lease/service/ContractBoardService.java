package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.mapper.ContractBoardMapper;
import cn.cuiot.dmp.lease.vo.ContractBoardInfoVo;
import cn.cuiot.dmp.lease.vo.ContractBoardInfoVoResult;
import cn.cuiot.dmp.lease.vo.ContractBoardVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Date 2024-10-22 11:25
 * @Author by Mujun~
 */
@Slf4j
@Service
public class ContractBoardService {
    @Autowired
    ContractBoardMapper contractBoardMapper;

    public Integer queryArchiveNum(Long companyId) {
        return contractBoardMapper.queryArchiveNum(companyId);
    }

    public Integer queryHouseNum(Long companyId) {
        return contractBoardMapper.queryHouseNum(companyId);
    }


    public Integer queryLeaseHouseNum(Long companyId) {
        return contractBoardMapper.queryLeaseHouseNum(companyId);
    }

    public Integer queryUnLeaseHouseNum(Long companyId) {
        return contractBoardMapper.queryUnLeaseHouseNum(companyId);
    }

    public Integer queryLeaseContractNum(Long companyId) {
        return contractBoardMapper.queryLeaseContractNum(companyId);
    }

    public Integer queryIntentionContractNum(Long companyId) {
        return contractBoardMapper.queryIntentionContractNum(companyId);
    }

    public List<ContractBoardInfoVo> queryLoupanBoardInfo(Long companyId) {
        return contractBoardMapper.queryLoupanBoardInfo(companyId);
    }

    public ContractBoardInfoVoResult queryContractBoardInfo() {
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        Integer loupanNum = queryArchiveNum(currentOrgId);
        Integer houseNum = queryHouseNum(currentOrgId);
        Integer leaseHouseNum = queryLeaseHouseNum(currentOrgId);
        Integer unLeaseHouseNum = queryUnLeaseHouseNum(currentOrgId);
        Integer leaseContractNum = queryLeaseContractNum(currentOrgId);
        Integer intentionContractNum = queryIntentionContractNum(currentOrgId);
        ContractBoardVo contractBoardVo = new ContractBoardVo();
        contractBoardVo.setLoupanNum(loupanNum);
        contractBoardVo.setHouseNum(houseNum);
        contractBoardVo.setLeaseHouseNum(leaseHouseNum);
        contractBoardVo.setUnleaseHouseNum(unLeaseHouseNum);
        contractBoardVo.setLeaseNum(leaseContractNum);
        contractBoardVo.setIntentionNum(intentionContractNum);
        List<ContractBoardInfoVo> contractBoardVos = queryLoupanBoardInfo(currentOrgId);

        ContractBoardInfoVoResult contractBoardInfoVoResult = new ContractBoardInfoVoResult();
        contractBoardInfoVoResult.setContractBoardVo(contractBoardVo);
        contractBoardInfoVoResult.setContractBoardInfoVo(contractBoardVos);
        return contractBoardInfoVoResult;


    }
}
