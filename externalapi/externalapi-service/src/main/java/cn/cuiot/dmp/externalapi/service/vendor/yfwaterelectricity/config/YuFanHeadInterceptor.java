package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 宇泛 请求头拦截器
 *
 * @author xiaotao
 * @date 2024/9/29 14:13
 */
@Component
public class YuFanHeadInterceptor implements RequestInterceptor {

    @Resource
    private YuFanProperties yuFanProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 添加宇泛请求头
        requestTemplate.header("token", yuFanProperties.getToken());
        requestTemplate.header("projectGuid", yuFanProperties.getProjectGuid());
    }

}
