package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.service.ApiPermissionService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author: wuyongchong
 * @date: 2024/4/25 16:18
 */
@Slf4j
public class BasePermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiPermissionService apiPermissionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        if (Objects.isNull(LoginInfoHolder.getCurrentUserId())) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermissions annotation = handlerMethod.getBeanType()
                .getAnnotation(RequiresPermissions.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
        }
        if (annotation == null) {
            return true;
        }
        String permissionCode = annotation.value();
        if (StringUtils.isBlank(permissionCode)) {
            permissionCode = request.getServletPath();
        }
        /**
         * 检测权限
         */
        apiPermissionService.checkApiPermission(
                permissionCode,
                LoginInfoHolder.getCurrentUserId().toString(),
                LoginInfoHolder.getCurrentOrgId().toString());

        return true;
    }
}
