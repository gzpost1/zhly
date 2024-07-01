package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanEntity;
import cn.cuiot.dmp.lease.service.charge.ChargeCollectionPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 催款计划-自动执行计费任务
 *
 * @Author: zc
 * @Date: 2024-06-28
 */
@Slf4j
@Component
public class ChargeCollectionPlanTask {

    @Autowired
    private ChargeCollectionPlanService chargeCollectionPlanService;

    /**
     * 每天生成计费任务
     *
     * @param param
     * @return
     */
    @XxlJob("createChargeCollectionPlainTaskByDay")
    public ReturnT<String> createChargeCollectionPlainTaskByDay(String param) {
        log.info("-------------催款计划-自动执行计费任务------begin-------------");
        ChargeCollectionPlanEntity planEntity = new ChargeCollectionPlanEntity();
        planEntity.setStatus(EntityConstants.ENABLED);

        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            IPage<ChargeCollectionPlanEntity> page =
                    chargeCollectionPlanService.queryEntityForPage(new Page<>(pageNo.getAndAdd(1), pageSize), planEntity);
            pages = page.getPages();
            if (CollectionUtils.isNotEmpty(page.getRecords())) {
                chargeCollectionPlanService.createChargePlainDayTask(page.getRecords());
            }
        } while (pageNo.get() <= pages);

        log.info("-------------催款计划-自动执行计费任务------end-------------");

        return ReturnT.SUCCESS;
    }
}