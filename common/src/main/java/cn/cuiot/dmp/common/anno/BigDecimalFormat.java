package cn.cuiot.dmp.common.anno;

import java.lang.annotation.*;

/**
 * 权限注解
 *
 * @author wuyongchong
 * @date 2020-09-07 16:51:30
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BigDecimalFormat {

    /**
     * 保留几位小数
     */
    String value() default "";


    /**
     * 0 =》 取一位整数
     * 0.00 =》 取一位整数和两位小数
     * 00.000 =》 取两位整数和三位小数
     * # =》 取所有整数部分
     * #.##% =》 以百分比方式计数，并取两位小数
     * #.#####E0 =》 显示为科学计数法，并取五位小数
     * ,### =》 每三位以逗号进行分隔，例如：299,792,458
     * 光速大小为每秒,###米 =》 将格式嵌入文本
     * @return
     */
    String numberFormat() default "2";

}
