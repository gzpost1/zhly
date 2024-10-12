package cn.cuiot.dmp.common.enums;

import static com.oracle.jrockit.jfr.ContentType.Bytes;

/**
 * @author xiaotao
 * @className LogLevelEnum
 * @description 数据库中启用 停用说明
 * @date 2020-09-07 11:43:17
 */
public enum StatusEnum {

    /**
     * 日志级别
     */
    ENABLE((byte) 1, "ENABLE"),
    DISABLE((byte) 0, "DISABLE");

    private Byte code;
    private String description;

    StatusEnum(Byte code, String name) {
        this.code = code;
        this.description = name;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
