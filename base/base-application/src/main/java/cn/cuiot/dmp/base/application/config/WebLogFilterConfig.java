package cn.cuiot.dmp.base.application.config;


import cn.cuiot.dmp.base.application.filter.WebLogFilter;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixf
 */
@Configuration
public class WebLogFilterConfig {

    /**
     * 注册 过滤器 Filter
     */
    @Bean
    public FilterRegistrationBean<Filter> webLogFilterConfigRegistration() {
        String urlPatterns = "/*";
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new WebLogFilter());
        registration.addUrlPatterns(urlPatterns);
        registration.setName("logExportFilter");
        registration.setOrder(3);
        registration.setEnabled(true);
        registration.addInitParameter("log", "true");
        return registration;
    }
}
