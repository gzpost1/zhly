package cn.cuiot.dmp.common.log.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author guoying
 * @className LogRecord
 * @description 日志记录注解
 * @date 2020-09-07 16:51:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {

    /**
     * 操作编码
     *
     * @return
     */
    String operationCode() default "";

    /**
     * 操作名称
     *
     * @return
     */
    String operationName() default "";

    /**
     * 业务类型
     *
     * @return
     */
    String serviceType() default "";

}
