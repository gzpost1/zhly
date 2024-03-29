package cn.cuiot.dmp.common.enums;

/**
 * Created by yth on 2023/08/07
 */
public enum EventActionEnum {
    CREATE("create", "创建"), UPDATE("update", "更新"), DELETE("delete", "删除"), REGISTER_SUCCESS("registerSuccess", "注册成功");

    EventActionEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;
    private String desc;

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
