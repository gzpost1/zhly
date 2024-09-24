package cn.cuiot.dmp.sms.vendor.interceptor;

import cn.cuiot.dmp.base.application.config.SmsProperties;
import cn.cuiot.dmp.common.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
public class SmsRemoteInterceptor implements RequestInterceptor {

    @Autowired
    private SmsProperties smsProperties;

    @SneakyThrows
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String secretName = smsProperties.getSecretName();
        String secretKey = smsProperties.getSecretKey();
        byte[] body = requestTemplate.request().body();
        Map<String, Object> paramMap = Maps.newHashMap();
        if (ArrayUtils.isNotEmpty(body)) {
            String strBody = new String(body, StandardCharsets.UTF_8);
            paramMap = JsonUtil.readValue(strBody, new TypeReference<Map<String, Object>>() {});
            log.debug("reqBody：" + strBody);
        }
        paramMap.put("SecretName", secretName);
        paramMap.put("SecretKey", secretKey);
        paramMap.put("TimeStamp", System.currentTimeMillis());
        String reqBody = JsonUtil.writeValueAsString(paramMap);

        requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        requestTemplate.body(reqBody);
    }
}
