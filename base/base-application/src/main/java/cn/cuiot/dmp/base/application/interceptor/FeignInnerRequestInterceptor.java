package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.base.application.constant.FeignConstants;
import cn.cuiot.dmp.domain.types.AuthContants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *  Feign内部请求拦截器
 * @author: wuyongchong
 * @date: 2024/5/10 15:34
 */
@Component
public class FeignInnerRequestInterceptor implements RequestInterceptor {

    @Autowired
    private AppProperties appProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //设置内部token
        requestTemplate.header(FeignConstants.INNER_TOKEN_NAME,appProperties.getAccessToken());
        //设置用户token
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String jwt = request.getHeader(AuthContants.TOKEN);
        if(StringUtils.isBlank(jwt)){
            jwt = request.getHeader(AuthContants.AUTHORIZATION);
        }
        requestTemplate.header(AuthContants.AUTHORIZATION, jwt);
    }
}
