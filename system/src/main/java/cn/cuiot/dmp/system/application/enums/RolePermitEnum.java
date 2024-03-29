package cn.cuiot.dmp.system.application.enums;

/**
 * @author guoying
 * @className RolePermitEnum
 * @description 角色权限枚举
 * @date 2020-08-13 19:43:20
 */
public enum RolePermitEnum {

    /**
     * 超级管理员(物联网总部管理员)
     */
    SUPER_ADMIN(1,"超级管理员"),

    /**
     * 系统管理员
     */
    SUPER_OPERATOR(2,"系统管理员"),

    /**
     * 系统普通管理员(省份)
     */
    PROVINCE_ADMIN(3,"系统普通管理员"),

    /**
     * 企业管理员
     */
    ADMIN(4, "管理员"),

    /**
     * 只读
     */
    VIEW_ONLY(5, "只读"),

    /**
     * 自定义
     */
    CUSTOMIZE(6,"自定义");

    private Integer code;
    private String name;

    RolePermitEnum(Integer code, String name) {
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

    public static String getRolePermitNameByPermitCode(Integer code) {
        if (null != code) {
            RolePermitEnum[] rolePermitEnums = RolePermitEnum.values();
            for (RolePermitEnum permitEnum : rolePermitEnums) {
                if (permitEnum.code.equals(code)) {
                    return permitEnum.name();
                }
            }
        }

        return null;
    }

}
