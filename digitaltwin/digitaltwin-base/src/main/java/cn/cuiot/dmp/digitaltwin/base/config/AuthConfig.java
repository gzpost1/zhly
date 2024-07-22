package cn.cuiot.dmp.digitaltwin.base.config;

/**
 * @author: wuyongchong
 * @date: 2024/2/28 9:17
 */

import cn.cuiot.dmp.digitaltwin.base.auth.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AuthConfig {

}
