package cn.cuiot.dmp.system.application.enums;

/**
 * @Description
 * @Author cds
 * @Date 2020/9/8
 */
public enum ResetPasswordEnum {

    /**
     * 下次登录不需要重置
     */
    NO_RESET(0, "下次登录不需要重置"),

    /**
     * 下次登录需要重置
     */
    RESET(1, "下次登录需要重置"),

    /**
     * 已经重置
     */
    ALREADY_RESET(2, "已经重置");

    private Integer code;
    private String name;

    ResetPasswordEnum(Integer code, String name) {
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
