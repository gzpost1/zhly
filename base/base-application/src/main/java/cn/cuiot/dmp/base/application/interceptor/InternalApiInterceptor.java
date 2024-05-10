package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.base.application.constant.FeignConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 服务间调用鉴权拦截器
 *
 * @author: wuyongchong
 * @date: 2024/4/25 16:18
 */
@Slf4j
public class InternalApiInterceptor implements HandlerInterceptor {

    @Autowired
    private AppProperties appProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        InternalApi annotation = handlerMethod.getBeanType()
                .getAnnotation(InternalApi.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(InternalApi.class);
        }
        if (annotation == null) {
            return true;
        }
        String accessToken = request.getHeader(FeignConstants.INNER_TOKEN_NAME);
        if (!appProperties.getAccessToken().equals(accessToken)) {
            log.error("InternalApiInterceptor accessToken error");
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        return true;
    }
}
