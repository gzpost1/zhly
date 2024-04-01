package cn.cuiot.dmp.upload.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wuyongchong
 * @date: 2023/10/12 11:35
 */
@Configuration
@EnableConfigurationProperties({OssProperties.class})
public class OssConfiguration {

}
