package cn.cuiot.dmp.digitaltwin.base.config;

import cn.cuiot.dmp.digitaltwin.base.auth.AuthProperties;
import cn.cuiot.dmp.digitaltwin.base.interceptor.AuthInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 安全配置
 *
 * @Author: zc
 * @Date: 2024-06-17
 */
@Configuration
@AutoConfigureAfter(AuthConfig.class)
public class SecurityConfig implements WebMvcConfigurer {

    private final AuthProperties authProperties;

    public SecurityConfig(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(authProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(
                authInterceptor());
        interceptorRegistration.addPathPatterns("/**");
    }
}
