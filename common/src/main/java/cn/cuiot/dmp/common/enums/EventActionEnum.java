package cn.cuiot.dmp.common.enums;

/**
 * Created by yth on 2023/08/07
 */
public enum EventActionEnum {
    CREATE("create", "新增"),
    UPDATE("update", "编辑"),
    DELETE("delete", "删除"),
    ENABLE("enable", "启用"),
    DISABLE("disable", "停用"),
    REGISTER_SUCCESS("registerSuccess", "注册成功");

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
