package cn.cuiot.dmp.lease.enums;

/**
 * 收费管理-收银台-缴费管理-收费方式 0自然月周期 1临时收费
 */
public enum ChargeReceivedTypeEnum {
    NATURAL_MONTH_CYCLE((byte) 0, "自然月周期"),
    TEMPORARY_CHARGE((byte) 1, "临时收费");

    private Byte code;
    private String desc;

    ChargeReceivedTypeEnum(Byte code, String desc) {
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
