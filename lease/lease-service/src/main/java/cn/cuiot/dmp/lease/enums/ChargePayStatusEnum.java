package cn.cuiot.dmp.lease.enums;

/**
 * @Description 0待支付 1支付取消 2支付失败 3支付成功
 * @Date 2024/10/10 19:20
 * @Created by libo
 */
public enum ChargePayStatusEnum {
    WAIT_PAY((byte)0, "待支付"),
    PAY_CANCEL((byte)1, "支付取消"),
    PAY_FAIL((byte)2, "支付失败"),
    PAY_SUCCESS((byte)3, "支付成功");

    private Byte code;
    private String desc;

    ChargePayStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }
}
