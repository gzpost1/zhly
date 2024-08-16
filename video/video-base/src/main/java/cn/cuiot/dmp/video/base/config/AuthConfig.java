package cn.cuiot.dmp.video.base.config;

/**
 * @Author: zc
 * @Date: 2024-08-16
 */

import cn.cuiot.dmp.video.base.auth.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AuthConfig {

}
