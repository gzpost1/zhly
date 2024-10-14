package cn.cuiot.dmp.lease.enums;

import com.google.common.collect.Lists;

import java.util.Objects;

/**
 * 收费管理-收银台-缴费管理-应收状态 0未交款 1已交款 2已交清
 */
public enum ChargeReceivbleEnum {
    UNPAID((byte) 0, "未交款"),
    PAID((byte) 1, "已交款"),
    PAID_OFF((byte) 2, "已交清");

    private Byte code;
    private String desc;

    ChargeReceivbleEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取desc
     * @param code
     * @return
     */
    public static String getDesc(Byte code){
        for (ChargeReceivbleEnum chargeEnum:ChargeReceivbleEnum.values()){
            if(Objects.equals(code,chargeEnum.getCode())){
                return chargeEnum.getDesc();
            }
        }

        return null;
    }
    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    //进行作废操作，已开交、已交清的状态不显示作废按钮
    public static boolean isShowAbrogate(Byte code) {
        return Lists.newArrayList(PAID_OFF.getCode(), PAID.getCode()).contains(code);
    }
}
