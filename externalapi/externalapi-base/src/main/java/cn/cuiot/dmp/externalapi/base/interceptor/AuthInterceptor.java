package cn.cuiot.dmp.externalapi.base.interceptor;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.externalapi.base.auth.AuthProperties;
import cn.cuiot.dmp.externalapi.base.auth.ThirdPushNeedAuth;
import cn.cuiot.dmp.externalapi.base.auth.ThirdRequestNeedAuth;
import cn.cuiot.dmp.externalapi.base.constant.Constant;
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

        boolean pushToken = checkPushToken(handlerMethod, request);
        boolean requestToken = checkRequestToken(handlerMethod, request);

        if (!pushToken && !requestToken) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "accessToken校验失败");
        }
        return true;
    }

    /**
     * 校验推送token
     */
    private boolean checkPushToken(HandlerMethod handlerMethod, HttpServletRequest request) {
        ThirdPushNeedAuth annotation = handlerMethod.getBeanType().getAnnotation(ThirdPushNeedAuth.class);
        if (Objects.isNull(annotation)) {
            annotation = handlerMethod.getMethodAnnotation(ThirdPushNeedAuth.class);
        }
        if (Objects.isNull(annotation)) {
            return true;
        }
        //校验内部访问token
        String accessToken = request.getHeader(Constant.PUSH_ACCESS_TOKEN_HEADER_KEY);
        if (StringUtils.isBlank(accessToken)) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "accessToken不能为空");
        }
        return Objects.equals(accessToken, authProperties.getPushAccessToken());
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
