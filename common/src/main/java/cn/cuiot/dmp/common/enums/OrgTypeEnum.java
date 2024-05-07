package cn.cuiot.dmp.common.enums;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:46
 * @Version V1.0
 */
public enum OrgTypeEnum {

    PLATFORM(5L, "平台账户"),

    ENTERPRISE(11L, "企业账户");

    private final Long value;
    private final String desc;

    OrgTypeEnum(Long value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Long getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrgTypeEnum valueOf(Long type) {
        for (OrgTypeEnum value : values()) {
            if (value.getValue().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
