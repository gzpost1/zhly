package cn.cuiot.dmp.common.enums;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 枚举值校验实现
 *
 * @author lixf
 */
@Slf4j
public class EnumValidatorClass implements ConstraintValidator<EnumValidator, Object>, Annotation {
    private final List<Object> values = new ArrayList<>();

    @Override
    public void initialize(EnumValidator enumValidator) {
        Class<?> clz = enumValidator.value();
        Object[] ojects = clz.getEnumConstants();
        try {
            Method method = clz.getMethod("getValue");
            Object value = null;
            for (Object obj : ojects) {
                value = method.invoke(obj);
                values.add(value);
            }
        } catch (Exception e) {
            log.error("[处理枚举校验异常]", e);
        }
    }


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.isNull(value) || values.contains(value);
    }
}
