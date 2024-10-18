package cn.cuiot.dmp.lease.enums;


import java.util.Objects;

/**
 * 执行频率 1:每天，2:每周，3:每月
 *
 * @author zc
 */
public enum ChargeCollectionPlainCronTypeEnum {
    /**
     * 每日
     */
    DAILY((byte) 1, "每天"),
    /**
     * 每周
     */
    WEEKLY((byte) 2, "每周"),
    /**
     * 每月
     */
    MONTHLY((byte) 3, "每月");

    private Byte code;
    private String desc;

    ChargeCollectionPlainCronTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ChargeCollectionPlainCronTypeEnum getByCode(byte code) {
        for (ChargeCollectionPlainCronTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}