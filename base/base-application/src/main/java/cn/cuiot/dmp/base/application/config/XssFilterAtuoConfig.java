package cn.cuiot.dmp.base.application.config;

import cn.cuiot.dmp.base.application.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xss 自动配置类
 *
 * @author: wuyongchong
 * @date: 2024/5/15 16:23
 */
@Configuration
public class XssFilterAtuoConfig {

    /*@Bean
    public FilterRegistrationBean xssFiltrRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("XssFilter");
        registration.setOrder(1);
        return registration;
    }*/

}