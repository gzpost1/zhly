package cn.cuiot.dmp.lease.enums;

import java.util.Objects;

/**
 * @Description 押金管理状态 0未交款 1已交清 2未退完 3 已退完 4作废
 * @Date 2024/6/14 10:44
 * @Created by libo
 */
public enum SecurityDepositStatusEnum {
    UNPAID((byte) 0, "未交款"),
    PAID_OFF((byte) 1, "已交清"),
    NOT_REFUNDED((byte) 2, "未退完"),
    REFUNDED((byte) 3, "已退完"),
    CANCELLED((byte) 4, "作废");

    private Byte code;
    private String desc;

    SecurityDepositStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(Byte code){
        for(SecurityDepositStatusEnum statusEnum :SecurityDepositStatusEnum.values()){
            if(Objects.equals(code,statusEnum.getCode())){
                return statusEnum.getDesc();
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
}
