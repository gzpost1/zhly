package cn.cuiot.dmp.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @param
 * @Author xieSH
 * @Description 路由转发配置
 * @Date 2021/11/4 16:44
 * @return
 **/
@Configuration
public class RouteConfig {

    /*@Bean
    public RouteLocator gatewayLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("community-system",
                        r ->
                                r.path("/community-system/**")
                                        .filters(f -> f.stripPrefix(1))
                                .uri("lb://community-system")
                ).build();
    }*/
}
