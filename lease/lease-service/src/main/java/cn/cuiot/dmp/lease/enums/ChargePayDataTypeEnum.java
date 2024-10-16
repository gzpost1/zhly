package cn.cuiot.dmp.lease.enums;

/**
 * @Description 0房屋账单 1房屋押金
 * @Date 2024/10/10 20:32
 * @Created by libo
 */
public enum ChargePayDataTypeEnum {
    HOUSE_BILL((byte)0, "房屋账单"),
    HOUSE_DEPOSIT((byte)1, "房屋押金");
    private Byte code;
    private String desc;

    ChargePayDataTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
