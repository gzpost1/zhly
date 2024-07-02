package cn.cuiot.dmp.lease.enums;


import java.util.Objects;

/**
 * 收费管理-催款计划-通知渠道
 *
 * @author zc
 */
public enum ChargeCollectionPlainChannelEnum {
    /**
     * 短信
     */
    SMS((byte) 1, "短信"),
    /**
     * 微信
     */
    WECHAT((byte) 2, "微信");

    private Byte code;
    private String desc;

    ChargeCollectionPlainChannelEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ChargeCollectionPlainChannelEnum getByCode(byte code) {
        for (ChargeCollectionPlainChannelEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}