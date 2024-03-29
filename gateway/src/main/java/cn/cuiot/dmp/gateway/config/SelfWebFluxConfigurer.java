package cn.cuiot.dmp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
* @描述:
*
* @Author:  sunking
* @Date:    2020/7/22 1:42 PM
*/
@Configuration
public class SelfWebFluxConfigurer implements WebFluxConfigurer {

    @Bean
    public CorsResponseHeaderFilter corsResponseHeaderFilter() {
        return new CorsResponseHeaderFilter();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        System.out.println("cors初始化");
        return new CorsWebFilter(source);

    }

}
