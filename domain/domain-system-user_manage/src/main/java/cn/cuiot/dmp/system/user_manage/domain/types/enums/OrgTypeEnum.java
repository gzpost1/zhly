package cn.cuiot.dmp.system.user_manage.domain.types.enums;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:46
 * @Version V1.0
 */
public enum OrgTypeEnum {

    PRIVATE(1L, "个人账户"),

    ENTERPRISE(2L, "企业账户"),

    CHILD(3L, "子账户"),

    SUPER(4L, "超级账户"),

    PROVINCE(5L, "省份账户"),

    COMMUNITY(11L, "物业账户"),

    COMMON(12L, "通用账户"),

    PERSONAL(13L, "小程序个人账户");

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

    public static OrgTypeEnum valueOf(Long userType) {
        for (OrgTypeEnum value : values()) {
            if (value.getValue().equals(userType)) {
                return value;
            }
        }
        return null;
    }
}
