package cn.cuiot.dmp.common.aop;


import cn.cuiot.dmp.common.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.controller.BaseController;
import cn.cuiot.dmp.common.service.ApiPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wen
 * @className LogRecordAspect
 * @description
 * @date 2020-09-07 17:01:43
 */
@Slf4j
@Order(3)
@Aspect
@Component
public class ApiPermissionAdvice extends BaseController {

    @Autowired
    ApiPermissionService permissionService;

    @Pointcut("@annotation(cn.cuiot.dmp.common.annotation.RequiresPermissions)")
    public void apiPermissionControl() {
    }

    @Before("apiPermissionControl()")
    public void before(JoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequiresPermissions permission = signature.getMethod().getAnnotation(RequiresPermissions.class);
        if (permission != null && StringUtils.hasLength(permission.value())) {
            String userId = getUserId();
            String orgId = getOrgId();
            permissionService.checkApiPermission(permission.value(), userId, orgId);
        }
    }
}
