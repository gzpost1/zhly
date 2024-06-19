package cn.cuiot.dmp.lease.enums;

/**
 * @Description 收费管理-收银台-缴费管理-所属类型 0手动创建  1自动生成
 * @Date 2024/6/12 17:48
 * @Created by libo
 */
public enum ChargeTypeEnum {
    MANUAL_CREATE((byte) 0, "手动创建"),
    AUTO_GENERATE((byte) 1, "自动生成");

    private Byte code;
    private String desc;

    ChargeTypeEnum(Byte code, String desc) {
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
