package cn.cuiot.dmp.gateway.config;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 网关相关配置
 *
 * @author: wuyongchong
 * @date: 2024/2/19 13:29
 */
@Configuration
public class GatewayConfig {

    @Resource
    private WebCorsProperties webCorsProperties;

    /**
     * 跨域配置
     */
    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(webCorsProperties.getAllowCredentials());
        config.setAllowedHeaders(webCorsProperties.getAllowedHeaders());
        config.setAllowedMethods(webCorsProperties.getAllowedMethods());
        config.setAllowedOriginPatterns(webCorsProperties.getAllowedOrigins());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
