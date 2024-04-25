package cn.cuiot.dmp.base.application.config;

import cn.cuiot.dmp.base.application.interceptor.BaseAuthInterceptor;
import cn.cuiot.dmp.base.application.interceptor.LogInterceptor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * BaseWebConfiguration
 *
 * @author: wuyongchong
 * @date: 2024/4/25 11:46
 */
@Configuration
public class BaseWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(logInterceptor);
        interceptorRegistration.addPathPatterns("/**");
        registry.addInterceptor(baseAuthInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public BaseAuthInterceptor baseAuthInterceptor() {
        return new BaseAuthInterceptor();
    }

}
