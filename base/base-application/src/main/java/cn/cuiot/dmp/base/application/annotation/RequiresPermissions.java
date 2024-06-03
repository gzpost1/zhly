package cn.cuiot.dmp.base.application.annotation;

import static cn.cuiot.dmp.base.application.constant.PermissionContants.USER_STAFF;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 *
 * @author wuyongchong
 * @date 2020-09-07 16:51:30
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /**
     * 权限编码
     */
    String value() default "";

    /**
     * 允许访问的用户类型
     */
    String allowUserType() default USER_STAFF;
}
