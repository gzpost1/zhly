package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.lease.entity.PriceManageEntity;
import cn.cuiot.dmp.lease.enums.PriceManageStatusEnum;
import cn.cuiot.dmp.lease.service.PriceManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author caorui
 * @date 2024/7/3
 */
@Slf4j
@Component
public class PriceManageTask {

    @Autowired
    private PriceManageService priceManageService;

    /**
     * 每天凌晨早上两点触发，审核通过的定价单，到达设置的执行日期后，变为已执行状态
     */
    @XxlJob("priceManageChangeStatusJobHandler")
    public ReturnT<String> priceManageChangeStatusJobHandler(String paramStr) {
        log.info("开始批量处理审核通过的定价单数据.............");
        try {
                LocalDate now = LocalDate.now();
                LambdaQueryWrapper<PriceManageEntity> queryWrapper = new LambdaQueryWrapper<PriceManageEntity>()
                        .eq(PriceManageEntity::getStatus, PriceManageStatusEnum.PASS_STATUS.getCode())
                        .eq(PriceManageEntity::getExecuteDate, DateTimeUtil.localDateToString(now, DateTimeUtil.DEFAULT_DATE_FORMAT));
                List<PriceManageEntity> priceManageEntityList = priceManageService.list(queryWrapper);
                if (CollectionUtils.isNotEmpty(priceManageEntityList)) {
                    priceManageEntityList.forEach(o -> o.setStatus(PriceManageStatusEnum.EXECUTED_STATUS.getCode()));
                    priceManageService.updateBatchById(priceManageEntityList);
                }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        log.info("审核通过的定价单数据处理结束.............");
        return ReturnT.SUCCESS;
    }

}
