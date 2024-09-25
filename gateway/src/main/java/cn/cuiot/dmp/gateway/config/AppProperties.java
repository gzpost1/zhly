package cn.cuiot.dmp.gateway.config;

import java.util.List;
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
     * 是否开启内部token访问
     */
    private Boolean enableGatewayAccessTokenRequest;

}
