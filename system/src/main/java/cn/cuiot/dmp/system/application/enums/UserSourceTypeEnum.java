package cn.cuiot.dmp.system.application.enums;

/**
 * @author guoying
 * @className UserTypeEnum
 * @description 创建修改者来源
 * @date 2020-08-21 10:34:55
 */
public enum UserSourceTypeEnum {

    /**
     * 用户来源类型
     */
    SYSTEM(1, "SYSTEM"),
    PORTAL(2, "平台(自注册)");

    private Integer code;
    private String name;

    UserSourceTypeEnum(Integer code, String name) {
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
