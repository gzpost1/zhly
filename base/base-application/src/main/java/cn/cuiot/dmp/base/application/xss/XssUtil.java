package cn.cuiot.dmp.base.application.xss;

import java.util.Objects;

/**
 * xss工具类
 * @author: wuyongchong
 * @date: 2024/5/15 16:21
 */
public class XssUtil {

    public static String cleanXSS(String value) {
        if (Objects.isNull(value)) {
            return value;
        }
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    public static String filter(String input) {
        if (Objects.isNull(input)) {
            return input;
        }
        return new HTMLFilter().filter(input);
    }
}