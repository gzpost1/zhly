package cn.cuiot.dmp.system.application.enums;

/**
 * @author xieshihai
 * @version 1.0
 * @Description 组织分组enum
 * @date 2021/12/29 10:42
 */
public enum DepartmentGroupEnum {
    /**
     * 系统
     */
    SYSTEM(1, "系统"),
    /**
     * 租户
     */
    TENANT(2, "租户"),
    /**
     * 小区
     */
    COMMUNITY(3, "小区"),
    /**
     * 楼栋
     */
    BUILDING(4, "楼栋"),
    /**
     * 房屋
     */
    HOUSE(5, "房屋"),
    /**
     * 区域
     */
    REGION(6, "区域"),

    /**
     * 楼层
     */
    FLOOR(7, "楼层");

    private Integer code;

    private String name;

    DepartmentGroupEnum(Integer code, String name) {
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

    public static DepartmentGroupEnum getGroupByCode(Integer code) {
        for (DepartmentGroupEnum value : DepartmentGroupEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
