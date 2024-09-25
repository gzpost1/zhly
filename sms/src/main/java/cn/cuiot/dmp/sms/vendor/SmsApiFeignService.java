package cn.cuiot.dmp.sms.vendor;

import cn.cuiot.dmp.sms.vendor.req.*;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsReportResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsSignStateResp;
import cn.cuiot.dmp.sms.vendor.resp.SmsTemplateStateResp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 短信-相关API
 *
 * @Author: zc
 * @Date: 2024-09-25
 */
@Service
public class SmsApiFeignService {

    @Autowired
    private SmsHttpApi smsHttpApi;

    /**
     * 发送短信
     */
    public SmsBaseResp<String> send(SmsSendReq req) {
        String gateway = "/Send";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<String>>() {
        });
    }

    /**
     * 获取报告
     */
    public SmsBaseResp<List<SmsReportResp>> getReport(SmsSendReq req) {
        String gateway = "/GetReport";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<List<SmsReportResp>>>() {
        });
    }

    /**
     * 查询余额
     */
    public SmsBaseResp<Integer> getBalance() {
        String gateway = "/GetBalance";
        return smsHttpApi.request(gateway, HttpMethod.POST, null, new TypeReference<SmsBaseResp<Integer>>() {
        });
    }

    /**
     * 申请模板
     */
    public SmsBaseResp<Integer> bindTemplate(SmsBindTemplateReq req) {
        String gateway = "/BindTemplate";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<Integer>>() {
        });
    }

    /**
     * 查询模板状态
     */
    @PostMapping(value = "/SmsTemplateState")
    public SmsBaseResp<List<SmsTemplateStateResp>> smsTemplateState(SmsTemplateStateReq req) {
        String gateway = "/SmsTemplateState";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<List<SmsTemplateStateResp>>>() {
        });
    }

    /**
     * 申请签名
     */
    public SmsBaseResp<Integer> bindSign(SmsBindSignReq req) {
        String gateway = "/BindSign";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<Integer>>() {
        });
    }

    /**
     * 查询签名状态
     */
    public SmsBaseResp<List<SmsSignStateResp>> getSignState(SmsSignStateReq req) {
        String gateway = "/GetSignState";
        return smsHttpApi.request(gateway, HttpMethod.POST, req, new TypeReference<SmsBaseResp<List<SmsSignStateResp>>>() {
        });
    }
}
