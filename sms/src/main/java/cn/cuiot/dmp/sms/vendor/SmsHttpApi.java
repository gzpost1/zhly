package cn.cuiot.dmp.sms.vendor;

import cn.cuiot.dmp.base.application.config.SmsProperties;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.entity.SmsConfigEntity;
import cn.cuiot.dmp.sms.enums.SmsHttpRespCode;
import cn.cuiot.dmp.sms.service.SmsConfigService;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zc
 * @Date: 2024-09-25
 */
@Slf4j
@Component
public class SmsHttpApi {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsConfigService smsConfigService;
    @Autowired
    private SmsProperties smsProperties;

    public <R, T> SmsBaseResp<R> request(String gateway, HttpMethod method, T data, TypeReference<SmsBaseResp<R>> reference) {

        HttpHeaders headers = new HttpHeaders();
        if (method == HttpMethod.POST) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        SmsConfigEntity smsConfigEntity = smsConfigService.queryRedisData();
        if (Objects.isNull(smsConfigEntity)) {
            throw new BusinessException(ResultCode.ERROR, "发送短信失败，密钥信息不存在");
        }
        if (StringUtils.isBlank(smsConfigEntity.getSecretKey())) {
            throw new BusinessException(ResultCode.ERROR, "发送短信失败，SecretKey不存在");
        }
        if (StringUtils.isBlank(smsConfigEntity.getSecretName())) {
            throw new BusinessException(ResultCode.ERROR, "发送短信失败，SecretName不存在");
        }

        String strBody = null;
        Map<String, Object> paramMap = Maps.newHashMap();
        if (Objects.nonNull(data)) {
            strBody = new String((byte[]) data, StandardCharsets.UTF_8);
            Map<String, Object> map = JsonUtil.readValue(strBody, new TypeReference<Map<String, Object>>() {
            });
            if (MapUtils.isNotEmpty(map)) {
                paramMap = map;
            }
        }
        paramMap.put("SecretName", smsConfigEntity.getSecretName());
        paramMap.put("SecretKey", smsConfigEntity.getSecretKey());
        paramMap.put("TimeStamp", System.currentTimeMillis());

        HttpEntity<?> httpEntity = new HttpEntity<>(paramMap, headers);

        String url = smsProperties.getUrl() + gateway;
        log.info("request requestData{},url:{}", strBody, url);

        SmsBaseResp<R> resp;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
            log.info("responseEntity body:{}", responseEntity.getBody());

            resp = JsonUtil.readValue(responseEntity.getBody(), reference);

        } catch (Exception e) {
            log.error("SmsHttpApi request error", e);
            throw new BusinessException(ResultCode.ERROR, "短信请求第三方接口失败");
        }

        if (Objects.isNull(resp)) {
            throw new BusinessException(ResultCode.ERROR, "短信请求第三方失败，返回内容为空");
        }

        if (!resp.isSuccess()) {
            if (Objects.nonNull(resp.getCode())) {
                SmsHttpRespCode itemByCode = SmsHttpRespCode.getItemByCode(resp.getCode());
                throw new BusinessException(itemByCode.getErrorCode(), resp.getMsg());
            }
            throw new BusinessException(ResultCode.ERROR, "短信请求第三方失败");
        }
        return resp;
    }
}
