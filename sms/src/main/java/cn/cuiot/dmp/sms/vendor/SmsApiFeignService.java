package cn.cuiot.dmp.sms.vendor;

import cn.cuiot.dmp.sms.vendor.interceptor.SmsRemoteInterceptor;
import cn.cuiot.dmp.sms.vendor.req.*;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsReportResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsSignStateResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsTemplateStateResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 短信
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Component
@FeignClient(value = "sms", url = "${sms.url}", configuration = SmsRemoteInterceptor.class)
public interface SmsApiFeignService {

    /**
     * 发送短信
     */
    @PostMapping(value = "/Send")
    SmsBaseResp<String> send(@RequestBody SmsSendReq req);

    /**
     * 获取报告
     */
    @PostMapping(value = "/GetReport")
    SmsBaseResp<List<SmsReportResp>> getReport(@RequestBody SmsSendReq req);

    /**
     * 查询余额
     */
    @PostMapping(value = "/GetBalance")
    SmsBaseResp<Integer> getBalance();

    /**
     * 申请模板
     */
    @PostMapping(value = "/BindTemplate")
    SmsBaseResp<Integer> bindTemplate(@RequestBody SmsBindTemplateReq req);

    /**
     * 查询模板状态
     */
    @PostMapping(value = "/SmsTemplateState")
    SmsBaseResp<List<SmsTemplateStateResp>> smsTemplateState(@RequestBody SmsTemplateStateReq req);

    /**
     * 申请签名
     */
    @PostMapping(value = "/BindSign")
    SmsBaseResp<Integer> bindSign(@RequestBody SmsBindSignReq req);

    /**
     * 查询签名状态
     */
    @PostMapping(value = "/GetSignState")
    SmsBaseResp<List<SmsSignStateResp>> getSignState(@RequestBody SmsSignStateReq req);
}
