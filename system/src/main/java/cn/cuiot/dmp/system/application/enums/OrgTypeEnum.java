package cn.cuiot.dmp.system.application.enums;

/**
 * @Description
 * @Author cds
 * @Date 2020/9/4
 */
public enum OrgTypeEnum {

    /**
     * 个人账户
     */
    PRIVATE(1, "个人账户"),

    /**
     * 企业账户
     */
    ENTERPRISE(2, "企业账户"),

    /**
     * 子账户
     */
    CHILD(3, "子账户"),

    /**
     * 超级账户
     */
    SUPER(4,"超级账户"),

    /**
     * 省份账户
     */
    PROVINCE(5,"联通账户"),

    /**
     * 物业账户id
     */
    COMMUNITY(11,"物业账户"),

    /**
     * 通用账户id
     */
    COMMON(12,"通用账户");

    private Integer code;
    private String name;


    OrgTypeEnum(Integer code, String name) {
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
