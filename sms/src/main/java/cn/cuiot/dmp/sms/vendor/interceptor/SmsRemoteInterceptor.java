package cn.cuiot.dmp.sms.vendor.interceptor;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.entity.SmsConfigEntity;
import cn.cuiot.dmp.sms.service.SmsConfigService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
@Component
public class SmsRemoteInterceptor implements RequestInterceptor {

    @Autowired
    private SmsConfigService smsConfigService;

    @SneakyThrows
    @Override
    public void apply(RequestTemplate requestTemplate) {
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

        byte[] body = requestTemplate.request().body();
        Map<String, Object> paramMap = Maps.newHashMap();
        if (ArrayUtils.isNotEmpty(body)) {
            String strBody = new String(body, StandardCharsets.UTF_8);
            paramMap = JsonUtil.readValue(strBody, new TypeReference<Map<String, Object>>() {
            });
            log.debug("reqBody：" + strBody);
        }
        paramMap.put("SecretName", smsConfigEntity.getSecretName());
        paramMap.put("SecretKey", smsConfigEntity.getSecretKey());
        paramMap.put("TimeStamp", System.currentTimeMillis());
        String reqBody = JsonUtil.writeValueAsString(paramMap);

        requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        requestTemplate.body(reqBody);
    }
}
