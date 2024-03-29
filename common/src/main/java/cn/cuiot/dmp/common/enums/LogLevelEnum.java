package cn.cuiot.dmp.common.enums;

/**
 * @author guoying
 * @className LogLevelEnum
 * @description 日志级别枚举
 * @date 2020-09-07 11:43:17
 */
public enum LogLevelEnum {

    /**
     * 日志级别
     */
    INFO("Info", "000000"),
    WARN("Warn", "其他设备日志状态码"),
    ERROR("Error", "6003,6002");

    private String code;
    private String description;

    LogLevelEnum(String code, String name) {
        this.code = code;
        this.description = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
