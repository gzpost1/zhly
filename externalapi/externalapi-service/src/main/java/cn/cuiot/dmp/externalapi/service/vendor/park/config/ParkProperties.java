package cn.cuiot.dmp.externalapi.service.vendor.park.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @create 2024/9/3 15:15
 */
@Data
@ConfigurationProperties(prefix = "park")
@Component
@RefreshScope
public class ParkProperties {

    /**
     * appId
     */
    private String appId;

    /**
     * appSercert
     */
    private String appSercert;

    /**
     * 请求路径
     */
    private String url;
}
