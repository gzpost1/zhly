package cn.cuiot.dmp.lease.enums;

/**
 * 收费管理-收银台-缴费管理-挂起状态 0未挂起 1已挂起
 */
public enum ChargeHangUpEnum {
    UNHANG_UP((byte) 0, "未挂起"),
    HANG_UP((byte) 1, "已挂起");

    private Byte code;
    private String desc;

    ChargeHangUpEnum(Byte code, String desc) {
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
