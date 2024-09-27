package cn.cuiot.dmp.gateway.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 跨域配置
 * @author: wuyongchong
 * @date: 2024/9/27
 */
@Component
@ConfigurationProperties(prefix = "web.cors")
@Data
@RefreshScope
public class WebCorsProperties {
    private List<String> allowedOrigins = new ArrayList();
    private List<String> allowedMethods = new ArrayList();
    private List<String> allowedHeaders = new ArrayList();
    private List<String> exposedHeaders = new ArrayList();
    private Boolean allowCredentials = true;
}
