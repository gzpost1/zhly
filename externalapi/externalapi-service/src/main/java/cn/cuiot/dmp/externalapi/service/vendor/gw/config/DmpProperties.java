
package cn.cuiot.dmp.externalapi.service.vendor.gw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 格物平台关键配置
 * @Date 2024-02-21 16:55
 * @Author by Mujun~
 */
@Data
@ConfigurationProperties(prefix = DmpProperties.PREFIX)
@Component
@RefreshScope
public class DmpProperties implements Serializable {
    
    public static final String PREFIX = "externalapi";

    @Value("${gw.baseurl}")
    private String baseurl;
}
