package cn.cuiot.dmp.gateway.config;

import cn.cuiot.dmp.gateway.dto.SignatureConfigDto;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 签名配置信息
 *
 * @author lixf
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "signature")
@Data
public class SignatureConfig {
    private Boolean status;
    private List<SignatureConfigDto> app;
}
