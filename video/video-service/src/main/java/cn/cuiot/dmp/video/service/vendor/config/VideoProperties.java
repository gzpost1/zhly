
package cn.cuiot.dmp.video.service.vendor.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 监控配置
 *
 * @Author: zc
 * @Date: 2024-03-05
 */
@Data
@ConfigurationProperties(prefix = VideoProperties.PREFIX)
@Component
@RefreshScope
public class VideoProperties implements Serializable {

    public static final String PREFIX = "video";

    /**
     * 云智眼baseUrl
     */
    @Value("${vsuap.baseUrl}")
    private String vsuapBaseUrl;

    /**
     * 云智眼AK
     */
    @Value("${vsuap.accessKey}")
    private String vsuapAccessKey;

    /**
     * 云智眼SK
     */
    @Value("${vsuap.secretKey}")
    private String vsuapSecretKey;
}
