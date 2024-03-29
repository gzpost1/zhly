package cn.cuiot.dmp.system.application.enums;

/**
 * @author wangqd
 * @className UserPropertyEnum
 * @description 创建修改者来源
 * @date 2020-08-21 10:34:55
 */
public enum UserPropertyEnum {
    /**
     * 创建修改者来源枚举值
     */
    SPACE_PATH("spacePath","场所路径")
            ;

    ;


    private String code;
    private String name;

    UserPropertyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
