package cn.cuiot.dmp.system.domain.types.enums;

/**
 * @Description 用户状态
 * @Author 犬豪
 * @Date 2023/8/28 11:21
 * @Version V1.0
 */
public enum UserStatusEnum {
    OPEN(1, "开启"), CLOSE(0, "关闭");

    UserStatusEnum(Integer value, String desc) {
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

    public static UserStatusEnum valueOf(Integer status) {
        for (UserStatusEnum value : values()) {
            if (value.getValue().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
