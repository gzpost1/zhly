package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import cn.cuiot.dmp.sms.entity.SmsTemplateEntity;
import cn.cuiot.dmp.sms.enums.SmsThirdStatusEnum;
import cn.cuiot.dmp.sms.service.SmsSignService;
import cn.cuiot.dmp.sms.service.SmsTemplateService;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.req.SmsSignStateReq;
import cn.cuiot.dmp.sms.vendor.req.SmsTemplateStateReq;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsSignStateResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsTemplateStateResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 短信定时任务
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
@Component
public class SmsTask {

    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private SmsSignService smsSignService;
    @Autowired
    private SmsApiFeignService smsApiFeignService;

    /**
     * 同步短信状态
     */
    @XxlJob("syncSmsTemplateState")
    public ReturnT<String> syncSmsTemplateState(String param) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            LambdaQueryWrapper<SmsTemplateEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SmsTemplateEntity::getThirdStatus, SmsThirdStatusEnum.UNAUDITED.getCode());
            Page<SmsTemplateEntity> page = smsTemplateService.page(new Page<>(pageNo.getAndAdd(1), pageSize), wrapper);
            // 获取总页数
            pages = page.getPages();
            List<SmsTemplateEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                //第三方模板id列表
                List<Integer> templateIds = records.stream().map(SmsTemplateEntity::getThirdTemplate).filter(Objects::nonNull).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(templateIds)) {
                    try {
                        // 请求第三方模板状态
                        SmsTemplateStateReq req = new SmsTemplateStateReq();
                        req.setTemplateIds(templateIds);
                        SmsBaseResp<List<SmsTemplateStateResp>> resp = smsApiFeignService.getTemplateState(req);

                        List<SmsTemplateStateResp> data = resp.getData();
                        Map<Integer, SmsTemplateStateResp> respMap = data.stream().collect(Collectors.toMap(SmsTemplateStateResp::getId, e -> e));
                        //设置审核状态、备注
                        List<SmsTemplateEntity> collect = records.stream().map(item -> {
                            if (Objects.nonNull(item.getThirdTemplate()) && respMap.containsKey(item.getThirdTemplate())) {
                                SmsTemplateStateResp respItem = respMap.get(item.getThirdTemplate());
                                item.setThirdStatus(respItem.getCheckType());
                                item.setRemark(respItem.getCheckRemark());
                                return item;
                            }
                            return null;
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                        // 更新状态
                        if (CollectionUtils.isNotEmpty(collect)) {
                            smsTemplateService.updateBatchById(collect);
                        }

                    } catch (Exception e) {
                        log.error("同步短信模板异常........");
                        e.printStackTrace();
                    }
                }
            }
        } while (pageNo.get() < pages);

        return ReturnT.SUCCESS;
    }

    /**
     * 同步短信状态
     */
    @XxlJob("syncSmsSignState")
    public ReturnT<String> syncSmsSignState(String param) {
        AtomicLong pageNo = new AtomicLong(1);
        long pageSize = 200;
        long pages;
        do {
            LambdaQueryWrapper<SmsSignEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SmsSignEntity::getThirdStatus, SmsThirdStatusEnum.UNAUDITED.getCode());
            Page<SmsSignEntity> page = smsSignService.page(new Page<>(pageNo.getAndAdd(1), pageSize), wrapper);
            // 获取总页数
            pages = page.getPages();
            List<SmsSignEntity> records;
            if (CollectionUtils.isNotEmpty(records = page.getRecords())) {
                // 第三方签名id列表
                List<Integer> templateIds = records.stream().map(SmsSignEntity::getThirdCode).filter(Objects::nonNull).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(templateIds)) {
                    try {
                        // 请求第三方签名状态
                        SmsSignStateReq req = new SmsSignStateReq();
                        req.setSignIds(templateIds);
                        SmsBaseResp<List<SmsSignStateResp>> resp = smsApiFeignService.getSignState(req);

                        List<SmsSignStateResp> data = resp.getData();
                        Map<Integer, SmsSignStateResp> respMap = data.stream().collect(Collectors.toMap(SmsSignStateResp::getId, e -> e));
                        //设置审核状态、备注
                        List<SmsSignEntity> collect = records.stream().map(item -> {
                            if (Objects.nonNull(item.getThirdCode()) && respMap.containsKey(item.getThirdCode())) {
                                SmsSignStateResp respItem = respMap.get(item.getThirdCode());
                                item.setThirdStatus(respItem.getCheckType());
                                item.setRemark(respItem.getCheckRemark());
                                return item;
                            }
                            return null;
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                        // 更新状态
                        if (CollectionUtils.isNotEmpty(collect)) {
                            smsSignService.updateBatchById(collect);
                        }

                    } catch (Exception e) {
                        log.error("同步短信签名异常........");
                        e.printStackTrace();
                    }
                }
            }
        } while (pageNo.get() < pages);

        return ReturnT.SUCCESS;
    }
}
