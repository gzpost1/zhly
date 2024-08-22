package cn.cuiot.dmp.externalapi.base.config;

/**
 * @Author: zc
 * @Date: 2024-08-16
 */

import cn.cuiot.dmp.externalapi.base.auth.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AuthConfig {

}
