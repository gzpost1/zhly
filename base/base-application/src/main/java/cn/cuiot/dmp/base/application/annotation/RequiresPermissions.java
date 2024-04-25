package cn.cuiot.dmp.base.application.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenshiqiang
 * @className LogRecord
 * @description 日志记录注解
 * @date 2020-09-07 16:51:30
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /**
     * 权限编码
     *
     * @return
     */
    String value() default "";
}
