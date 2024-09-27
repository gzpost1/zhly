package cn.cuiot.dmp.externalapi.base.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 第三方推送验证
 *
 * @Author: zc
 * @Date: 2024-06-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface ThirdPushNeedAuth {

}
