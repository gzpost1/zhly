package cn.cuiot.dmp.system.infrastructure.config;

import cn.cuiot.dmp.base.application.interceptor.LogInterceptor;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.domain.types.LoginInfo;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jz
 * @classname WebConfiguration
 * @description WebConfiguration
 * @date 2021/11/17
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(logInterceptor);
        interceptorRegistration.addPathPatterns("/**");
        registry.addInterceptor(logInfoInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public HandlerInterceptor logInfoInterceptor() {
        return new HandlerInterceptor() {

            private Logger logger = LoggerFactory.getLogger(this.getClass());
            private final LoginInfo.LazyPathFinder lazyPathFinder = lazyPathFinder();

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String jwt = request.getHeader("token");
                if (StringUtils.isNotBlank(jwt)) {
                    try {
                        Claims claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
                        Long userId = claims.get("userId") != null ? Long.valueOf(claims.get("userId").toString()) : null;
                        String userName = claims.get(Claims.SUBJECT).toString();
                        Long orgId = claims.get("org") != null ? Long.valueOf(claims.get("org").toString()) : null;
                        LoginInfo loginInfo = new LoginInfo(lazyPathFinder, userName, userId, orgId);
                        LoginInfoHolder.setLocalLoginInfo(loginInfo);
                    } catch (Exception e) {
                        logger.warn("logInfoInterceptor parse token error",e);
                    }
                }
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                LoginInfoHolder.clearLoginInfo();
            }
        };
    }


    @Bean
    public LoginInfo.LazyPathFinder lazyPathFinder() {
        return new LoginInfo.LazyPathFinder() {
            @Override
            public String lookUpUserPath(Long userId) {
                if (userId == null) {
                    return null;
                }
                //优先查询用户所在path
                DepartmentDto pathByUser = departmentService.getPathByUser(userId);
                return pathByUser != null ? pathByUser.getPath() : null;
            }

            @Override
            public String lookUpOrgPath(Long orgId) {
                if (orgId == null) {
                    return null;
                }
                List<DepartmentEntity> deptByOrgId = departmentService.getDeptByOrgId(orgId.toString());
                if (CollectionUtils.isEmpty(deptByOrgId)) {
                    return null;
                }
                return deptByOrgId.get(0).getPath();
            }
        };
    }

}
