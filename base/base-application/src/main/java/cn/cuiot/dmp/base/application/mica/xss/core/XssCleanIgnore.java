package cn.cuiot.dmp.base.application.mica.xss.core;

import java.lang.annotation.*;
/**
 * 忽略 xss
 * @author: wuyongchong
 * @date: 2024/5/16 11:04
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssCleanIgnore {

    /**
     * 支持指定忽略的字段，感谢 pig（冷冷）提出的需求
     *
     * @return 字段数组
     */
    String[] value() default {};

}