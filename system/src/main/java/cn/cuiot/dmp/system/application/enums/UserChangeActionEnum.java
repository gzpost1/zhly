package cn.cuiot.dmp.system.application.enums;

/**
 * 用户改变
 * @author liwit
 */

public enum UserChangeActionEnum {
    /**
     * 事件
     */
    ADD_ORG(0,"添加账户"),
    DEL_ORG(1, "删除账户"),
    ADD_USER(2, "添加用户"),
    DEL_USER(3, "删除用户")
    ;

    private Integer code;

    private String name;

    UserChangeActionEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
