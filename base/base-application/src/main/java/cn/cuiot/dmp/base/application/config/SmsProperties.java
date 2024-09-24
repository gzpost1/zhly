package cn.cuiot.dmp.base.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Component
@ConfigurationProperties(prefix = "sms")
@Data
@RefreshScope
public class SmsProperties {

    /**
     * 请求地址
     */
    private String url;

    /**
     * API密钥账号
     */
    private String secretName;

    /**
     * API密钥
     */
    private String secretKey;
}
