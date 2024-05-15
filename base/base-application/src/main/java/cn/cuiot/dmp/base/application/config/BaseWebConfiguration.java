package cn.cuiot.dmp.base.application.config;

import cn.cuiot.dmp.base.application.interceptor.BaseAuthInterceptor;
import cn.cuiot.dmp.base.application.interceptor.BaseLogInterceptor;
import cn.cuiot.dmp.base.application.interceptor.BasePermissionInterceptor;
import cn.cuiot.dmp.base.application.interceptor.InternalApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private BaseLogInterceptor baseLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(
                baseLogInterceptor);
        interceptorRegistration.addPathPatterns("/**");
        //增加服务间调用鉴权拦截器
        registry.addInterceptor(internalApiInterceptor());
        //增加登录用户信息解析拦截器
        registry.addInterceptor(baseAuthInterceptor());
        //增加登录用户权限校验拦截器
        registry.addInterceptor(basePermissionInterceptor());
    }

    @Bean
    public BaseAuthInterceptor baseAuthInterceptor() {
        return new BaseAuthInterceptor();
    }

    @Bean
    public BasePermissionInterceptor basePermissionInterceptor() {
        return new BasePermissionInterceptor();
    }

    @Bean
    public InternalApiInterceptor internalApiInterceptor() {
        return new InternalApiInterceptor();
    }

}
