package cn.cuiot.dmp.system.domain.types.enums;

import java.util.Arrays;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:36
 * @Version V1.0
 */
public enum UserTypeEnum {

    USER(1, "员工"),
    OWNER(2, "业主"),
    NULL(null, "userType为NULL的情况");

    UserTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getStrValue() {
        return String.valueOf(value);
    }

    public String getDesc() {
        return desc;
    }

    public static UserTypeEnum valueOf(Integer userType) {
        return Arrays.stream(values())
                .filter(userTypeEnum -> userTypeEnum != NULL && userTypeEnum.getValue().equals(userType)).findFirst()
                .orElse(NULL);
    }
}
