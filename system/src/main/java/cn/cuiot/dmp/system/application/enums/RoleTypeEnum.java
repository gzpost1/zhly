package cn.cuiot.dmp.system.application.enums;

/**
 * @author guoying
 * @className RolePermitEnum
 * @description 角色类型枚举
 * @date 2020-08-13 19:43:20
 */
public enum RoleTypeEnum {

    /**
     * 默认角色
     */
    DEFAULT(1,"默认角色"),

    /**
     * 自定义
     */
    CUSTOMIZE(2,"自定义");

    private Integer code;
    private String name;

    RoleTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
