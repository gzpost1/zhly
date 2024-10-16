package cn.cuiot.dmp.pay.service.service.enums;

import lombok.Getter;

/**
 * 余额变动类型
 **/
public enum BalanceChangeTypeEnum {

    BALANCE_PAY(Byte.valueOf("1"), "订单消费"),
    BALANCE_RECHARGE(Byte.valueOf("2"), "用户充值"),
    BALANCE_EXCHANGE(Byte.valueOf("3"), "积分兑换"),
    BALANCE_CONSUMPTION(Byte.valueOf("4"), "平台操作"),
    BALANCE_PAY_REFUND(Byte.valueOf("5"), "退款"),
    ;

    @Getter
    private Byte type;

    @Getter
    private String typeName;

    BalanceChangeTypeEnum(Byte type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static String getMessage(Byte status) {
        if (status == null) {
            return null;
        }
        for (BalanceChangeTypeEnum value : BalanceChangeTypeEnum.values()) {
            if (value.getType().equals(status)) {
                return value.getTypeName();
            }
        }
        return null;
    }

    public static BalanceChangeTypeEnum getTypeEnum(Byte status) {
        if (status == null) {
            return null;
        }
        for (BalanceChangeTypeEnum value : BalanceChangeTypeEnum.values()) {
            if (value.getType().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
