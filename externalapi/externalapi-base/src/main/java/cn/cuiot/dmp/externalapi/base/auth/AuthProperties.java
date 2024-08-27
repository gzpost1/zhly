package cn.cuiot.dmp.externalapi.base.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: zc
 * @Date: 2024-06-17
 */
@Component
@Data
@RefreshScope
@ConfigurationProperties(prefix = "user.auth")
public class AuthProperties {
    /**
     * 外部请求访问token
     */
    private String requestAccessToken;
}
