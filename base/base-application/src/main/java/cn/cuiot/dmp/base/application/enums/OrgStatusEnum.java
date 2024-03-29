package cn.cuiot.dmp.base.application.enums;

/**
 * @Description
 * @Author cds
 * @Date 2020/9/4
 */
public enum OrgStatusEnum {
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 启用
     */
    ENABLE(1, "启用");

    private Integer code;
    private String name;

    OrgStatusEnum(Integer code, String name) {
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
