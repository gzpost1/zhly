package cn.cuiot.dmp.base.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author: wuyongchong
 * @date: 2024/5/10 15:43
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
@RefreshScope
public class AppProperties {

    /**
     * 内部访问token
     */
    private String accessToken;

    /**
     * 开启菜单权限校验
     */
    private Boolean enablePermission;

    /**
     * 小程序appId
     */
    private String appId;

    /**
     * 小程序appSecret
     */
    private String appSecret;

    /**
     * 高德Key
     */
    private String amapKey;

}
