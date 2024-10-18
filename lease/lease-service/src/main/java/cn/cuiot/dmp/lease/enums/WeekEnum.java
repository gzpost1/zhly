package cn.cuiot.dmp.lease.enums;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/18 10:50
 */
public enum WeekEnum {

    /**
     * 每日
     */
    MONDAY(1, "星期一"),
    /**
     * 每周
     */
    TUESDAY(2, "星期二"),
    /**
     * 每月
     */
    WEDNESDAY(3, "星期三"),

    THURSDAY(4,"星期四"),

    FRIDAY(5,"星期五"),

    SATURDAY(6,"星期六"),

    SUNDAY(7,"星期日"),
    ;

    private Integer code;
    private String desc;

    WeekEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getByCode(Integer code) {
        for (WeekEnum weekEnum : WeekEnum.values()) {
            if (Objects.equals(weekEnum.code, code)) {
                return weekEnum.getDesc();
            }
        }
        return null;
    }
}
