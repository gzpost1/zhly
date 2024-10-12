package cn.cuiot.dmp.pay.service.service.config;

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
@ConfigurationProperties(prefix = "pay")
public class CommunityPayProperties {
    /**
     * 支付回调
     */
    private String notifyUrl;

}
