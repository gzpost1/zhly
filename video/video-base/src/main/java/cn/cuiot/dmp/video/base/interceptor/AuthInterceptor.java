package cn.cuiot.dmp.video.base.interceptor;

import cn.cuiot.dmp.video.base.auth.AuthProperties;
import cn.cuiot.dmp.video.base.auth.ThirdRequestNeedAuth;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.video.base.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author zc
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthProperties authProperties;

    public AuthInterceptor(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        boolean requestToken = checkRequestToken(handlerMethod, request);

        if (!requestToken) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "accessToken校验失败");
        }
        return true;
    }

    /**
     * 校验请求token
     */
    private boolean checkRequestToken(HandlerMethod handlerMethod, HttpServletRequest request) {
        ThirdRequestNeedAuth annotation = handlerMethod.getBeanType().getAnnotation(ThirdRequestNeedAuth.class);
        if (Objects.isNull(annotation)) {
            annotation = handlerMethod.getMethodAnnotation(ThirdRequestNeedAuth.class);
        }
        if (Objects.isNull(annotation)) {
            return true;
        }
        //校验内部访问token
        String accessToken = request.getHeader(Constant.REQUEST_ACCESS_TOKEN_HEADER_KEY);
        if (StringUtils.isBlank(accessToken)) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "accessToken不能为空");
        }
        return Objects.equals(accessToken, authProperties.getRequestAccessToken());
    }
}
