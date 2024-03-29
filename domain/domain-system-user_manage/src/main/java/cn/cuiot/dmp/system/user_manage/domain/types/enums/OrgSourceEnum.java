package cn.cuiot.dmp.system.user_manage.domain.types.enums;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:46
 * @Version V1.0
 */
public enum OrgSourceEnum {

    PRIVATE(0, "楼宇"),

    ENTERPRISE(1, "物联应用平台"),
    SELF_REGISTRATION(2, "自注册");

    private final Integer value;
    private final String desc;

    OrgSourceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrgSourceEnum valueOf(Integer userType) {
        for (OrgSourceEnum value : values()) {
            if (value.getValue().equals(userType)) {
                return value;
            }
        }
        return null;
    }
}
