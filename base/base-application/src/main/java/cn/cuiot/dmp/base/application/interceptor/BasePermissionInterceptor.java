package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.constant.PermissionContants;
import cn.cuiot.dmp.base.application.service.ApiPermissionService;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限校验拦截器
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
        if (Objects.isNull(LoginInfoHolder.getCurrentUserId())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        //允许访问的用户类型校验
        String allowUserType = annotation.allowUserType();
        Integer userType = LoginInfoHolder.getCurrentUserType();

        //只允许员工访问
        if(PermissionContants.USER_STAFF.equals(allowUserType)){
            if(!UserTypeEnum.USER.getValue().equals(userType)){
                //非员工客户，不能访问
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
        //只允许非员工客户访问
        if(PermissionContants.USER_CLIENT.equals(allowUserType)){
            if(UserTypeEnum.USER.getValue().equals(userType)){
                //员工不能访问
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
            return true;
        }
        //员工和非员工客户都能访问
        if(PermissionContants.USER_ALL.equals(allowUserType)){
            if(!UserTypeEnum.USER.getValue().equals(userType)){
                //非员工客户,直接放过
                return true;
            }
        }

        //获得权限值
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
