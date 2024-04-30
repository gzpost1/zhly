package cn.cuiot.dmp.system.domain.types.enums;

/**
 *
 * @description order by
 * @author pengcg
 * @date 2023/9/5
 */

public enum OrderByTypeEnum {

    ASC(0, "ASC"),

    DESC(1, "DESC"),
    ;

    OrderByTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderByTypeEnum valueOf(Integer type) {
        for (OrderByTypeEnum value : values()) {
            if (value.getValue().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
