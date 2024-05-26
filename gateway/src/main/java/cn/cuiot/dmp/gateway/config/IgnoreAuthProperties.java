package cn.cuiot.dmp.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "ignore.auth")
public class IgnoreAuthProperties {

    /**
     * 跳过鉴权的url
     */
    private List<String> urls;

}
