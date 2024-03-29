package cn.cuiot.dmp.system.application.enums;
/**
 * @author guoying
 * @className LogTypeEnum
 * @description 日志类型枚举
 * @date 2020-10-23 10:00:07
 */
public enum LogTypeEnum {
    /**
     * 日志类型枚举
     */
    OPERATION_LOG(3,"operationLog");
    private int code;
    private String desc;

    /**
     * 默认构造函数
     * @param code
     * @param desc
     */
    LogTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
