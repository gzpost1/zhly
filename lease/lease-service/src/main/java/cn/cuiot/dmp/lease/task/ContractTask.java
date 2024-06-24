package cn.cuiot.dmp.lease.task;//	模板

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.service.TbContractIntentionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ContractTask {

    @Autowired
    TbContractIntentionService contractIntentionService;

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
        queryWrapper.notIn(TbContractIntentionEntity::getContractStatus, ContractEnum.STATUS_SIGNING.getCode(),
                ContractEnum.STATUS_SIGNED.getCode(), ContractEnum.STATUS_CANCELING.getCode(), ContractEnum.STATUS_CANCELING.getCode(),
                ContractEnum.STATUS_USELESSING.getCode(), ContractEnum.STATUS_USELESS.getCode());
        LocalDate now = LocalDate.now();
        List<TbContractIntentionEntity> list = contractIntentionService.list(queryWrapper);
        list.forEach(c -> {
            LocalDate beginDate = c.getBeginDate();
            LocalDate endDate = c.getEndDate();
            //待执行
            if (beginDate.isAfter(now)) {
                //已过期
            } else if (endDate.isBefore(now)) {
                c.setContractStatus(ContractEnum.STATUS_EXPIRED.getCode());
                //执行中
            } else {
                c.setContractStatus(ContractEnum.STATUS_EXECUTING.getCode());
            }
        });
        contractIntentionService.saveOrUpdateBatch(list);
        log.info("--------------------更新意向合同状态 结束-------------------");
        return ReturnT.SUCCESS;
    }

}
