package cn.cuiot.dmp.system.domain.types.enums;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:47
 * @Version V1.0
 */
public enum OrgStatusEnum {
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 启用
     */
    ENABLE(1, "启用");

    OrgStatusEnum(Integer value, String desc) {
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

    public static OrgStatusEnum valueOf(Integer userType) {
        for (OrgStatusEnum value : values()) {
            if (value.getValue().equals(userType)) {
                return value;
            }
        }
        return null;
    }
}
