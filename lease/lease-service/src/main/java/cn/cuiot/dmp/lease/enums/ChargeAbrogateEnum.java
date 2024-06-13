package cn.cuiot.dmp.lease.enums;


/**
 * 收费管理-收银台-缴费管理-作废状态 0正常 1已作废
 */
public enum ChargeAbrogateEnum {
    NORMAL((byte) 0, "正常"),
    ABROGATE((byte) 1, "已作废");

    private Byte code;
    private String desc;

    ChargeAbrogateEnum(Byte code, String desc) {
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
