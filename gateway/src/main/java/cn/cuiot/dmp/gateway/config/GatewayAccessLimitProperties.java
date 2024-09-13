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
@ConfigurationProperties(prefix = "access.limit")
@Data
@RefreshScope
public class GatewayAccessLimitProperties {

    /**
     * 用户接口每分钟限制访问次数
     */
    private Integer userApiAccessLimit=5;

    /**
     * 特殊接口限制访问次数
     */
    private Integer specialApiAccessLimit=20;

    /**
     * 特殊接口限制访问的url
     */
    private List<String> specialApiAccessLimitUrls;

}
