package cn.cuiot.dmp.lease.enums;


import java.util.Objects;

/**
 * 收费管理-催款类型
 *
 * @author zc
 */
public enum ChargeCollectionTypeEnum {
    /**
     * 手动催款
     */
    MANUAL((byte) 1, "手动催款"),
    /**
     * 自动催款
     */
    AUTO((byte) 2, "自动催款");

    private Byte code;
    private String desc;

    ChargeCollectionTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ChargeCollectionTypeEnum getByCode(byte code) {
        for (ChargeCollectionTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}