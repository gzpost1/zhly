package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.lease.entity.charge.TbChargePlain;
import cn.cuiot.dmp.lease.enums.ChargePlainCronType;
import cn.cuiot.dmp.lease.service.charge.TbChargePlainService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Wrapper;
import java.util.Objects;

/**
 * @Description 自动执行计费任务
 * @Date 2024/6/20 11:30
 * @Created by libo
 */
@Slf4j
@Component
public class ChargePlainTask {
    @Autowired
    private TbChargePlainService chargePlainService;

    private static final Long PAGE_SIZE = 500L;
    /**
     * 每天生成计费任务
     *
     * @param param
     * @return
     */
    @XxlJob("createChargePlainTaskBYDay")
    public ReturnT<String> createDayWork(String param) {
        LambdaQueryWrapper<TbChargePlain> queryWrapper = Wrappers.<TbChargePlain>lambdaQuery().eq(TbChargePlain::getStatus, EntityConstants.ENABLED)
                .in(TbChargePlain::getCronType, ChargePlainCronType.dayCreate);

        long count = chargePlainService.count(queryWrapper);
        //求出总页数
        long totalPage = count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1);

        for (long i = 1; i <= totalPage; i++) {
            Page<TbChargePlain> page = chargePlainService.page(new Page<>(i, PAGE_SIZE), queryWrapper);
            if(Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())){
                Lists.partition(page.getRecords(), 100).forEach(e -> {
                    chargePlainService.createChargePlainDayTask(e);
                });
            }
        }

        return ReturnT.SUCCESS;
    }

    @XxlJob("createChargePlainTaskBYMonth")
    public ReturnT<String> createMonthWork(String param) {
        LambdaQueryWrapper<TbChargePlain> queryWrapper = Wrappers.<TbChargePlain>lambdaQuery().eq(TbChargePlain::getStatus, EntityConstants.ENABLED)
                .eq(TbChargePlain::getCronType, ChargePlainCronType.SPECIFIED_DATE.getCode());

        long count = chargePlainService.count(queryWrapper);
        //求出总页数
        long totalPage = count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1);

        for (long i = 1; i <= totalPage; i++) {
            Page<TbChargePlain> page = chargePlainService.page(new Page<>(i, PAGE_SIZE), queryWrapper);
            if(Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())){
                Lists.partition(page.getRecords(), 100).forEach(e -> {
                    chargePlainService.createChargePlainMonthTask(e);
                });
            }
        }

        return ReturnT.SUCCESS;
    }

}
