package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.base.application.constant.FeignConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        requestTemplate.header(FeignConstants.INNER_TOKEN_NAME,appProperties.getAccessToken());
    }
}
