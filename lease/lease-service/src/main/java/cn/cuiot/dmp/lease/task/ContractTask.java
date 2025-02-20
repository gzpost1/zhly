package cn.cuiot.dmp.lease.task;//	模板

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.BaseContractService;
import cn.cuiot.dmp.lease.service.TbContractIntentionService;
import cn.cuiot.dmp.lease.service.TbContractLeaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_INTENTION_TYPE;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_LEASE_TYPE;

@Component
@Slf4j
public class ContractTask {

    @Autowired
    TbContractIntentionService contractIntentionService;
    @Autowired
    TbContractLeaseService contractLeaseService;
    @Autowired
    BaseContractService baseContractService;

    /**
     * 定时更新意向合同状态
     *
     * @param param
     * @return
     */
    @XxlJob("更新意向合同状态")
    public ReturnT<String> syncContractIntentionStatus(String param) {
        log.info("--------------------更新意向合同状态 开始-------------------");
        LambdaQueryWrapper<TbContractIntentionEntity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.notIn(TbContractIntentionEntity::getContractStatus, ContractEnum.STATUS_SIGNING.getCode(),
//                ContractEnum.STATUS_SIGNED.getCode(), ContractEnum.STATUS_CANCELING.getCode(), ContractEnum.STATUS_CANCELING.getCode(),
//                ContractEnum.STATUS_USELESSING.getCode(), ContractEnum.STATUS_USELESS.getCode());
        queryWrapper.in(TbContractIntentionEntity::getContractStatus, ContractEnum.STATUS_WAITING.getCode(),
                ContractEnum.STATUS_EXECUTING.getCode(),ContractEnum.STATUS_EXPIRED.getCode());
        List<TbContractIntentionEntity> list = contractIntentionService.list(queryWrapper);
        list.forEach(c -> {
            LocalDate beginDate = c.getBeginDate();
            LocalDate endDate = c.getEndDate();
            Integer contractStatusByDate = baseContractService.handleContractStatusByDate(CONTRACT_INTENTION_TYPE, beginDate, endDate);
            c.setContractStatus(contractStatusByDate);
        });
        contractIntentionService.saveOrUpdateBatch(list);
        log.info("--------------------更新意向合同状态 结束-------------------");
        return ReturnT.SUCCESS;
    }
    /**
     * 定时更新租赁合同状态
     *
     * @param param
     * @return
     */
    @XxlJob("更新租赁合同状态")
    public ReturnT<String> syncContractLeaseStatus(String param) {
        log.info("--------------------更新租赁合同状态 开始-------------------");
        LambdaQueryWrapper<TbContractLeaseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TbContractLeaseEntity::getContractStatus, ContractEnum.STATUS_WAITING.getCode(),
                ContractEnum.STATUS_EXECUTING.getCode(),ContractEnum.STATUS_EXPIRED.getCode(),ContractEnum.STATUS_EXPIRED_WAITING.getCode());
        List<TbContractLeaseEntity> list = contractLeaseService.list(queryWrapper);
        list.forEach(c -> {
            LocalDate beginDate = c.getBeginDate();
            LocalDate endDate = c.getEndDate();
            Integer contractStatusByDate = baseContractService.handleContractStatusByDate(CONTRACT_LEASE_TYPE, beginDate, endDate);
            c.setContractStatus(contractStatusByDate);
        });
        contractLeaseService.saveOrUpdateBatch(list);
        log.info("--------------------更新租赁合同状态 结束-------------------");
        return ReturnT.SUCCESS;
    }

}
