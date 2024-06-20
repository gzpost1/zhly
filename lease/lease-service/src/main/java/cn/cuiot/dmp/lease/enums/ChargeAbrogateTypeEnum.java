package cn.cuiot.dmp.lease.enums;

/**
 * 作废数据类型 0缴费 1押金
 */
public enum ChargeAbrogateTypeEnum {
    CHARGE((byte) 0, "缴费"),
    DEPOSIT((byte) 1, "押金");

    private Byte code;
    private String desc;

    ChargeAbrogateTypeEnum(Byte code, String desc) {
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
