package cn.cuiot.dmp.common.enums;

/**
 * @author guoying
 * @className StatusCodeEnum
 * @description 状态码枚举
 * @date 2020-09-07 11:43:17
 */
public enum StatusCodeEnum {

    /**
     * 平台日志
     */
    SUCCESS("200", "成功"),
    FAILED("400", "失败");

    private String code;
    private String name;

    StatusCodeEnum(String code, String name) {
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
