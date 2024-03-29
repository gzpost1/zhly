package cn.cuiot.dmp.domain.types.enums;

/**
 * 操作人类型（创建者/更新者）
 *
 * @Author 犬豪
 * @Date 2023/8/28 11:29
 * @Version V1.0
 */
public enum OperateByTypeEnum {

    SYSTEM(1, "系统"),

    USER(2, "用户");

    OperateByTypeEnum(Integer value, String desc) {
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

    public static OperateByTypeEnum valueOf(Integer type) {
        for (OperateByTypeEnum value : values()) {
            if (value.getValue().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
