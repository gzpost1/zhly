package cn.cuiot.dmp.common.utils;

/**
 * @author liujianyu
 * @description 校验double类型
 * @since 2024-05-15 15:35
 */
public class DoubleValidator {
    public static boolean validateDouble(Double value, Integer length, Integer decimalLen) {
        String stringValue = String.valueOf(value);

        // 校验是否小于等于15位
        if (stringValue.length() > length) {
            return false;
        }

        // 校验小数位数是否不超过4位
        int decimalPlaces = stringValue.length() - stringValue.indexOf('.') - 1;
        if (decimalPlaces > decimalLen) {
            return false;
        }

        return true;
    }
}
