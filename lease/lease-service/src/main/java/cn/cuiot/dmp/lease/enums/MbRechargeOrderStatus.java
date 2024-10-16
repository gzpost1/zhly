package cn.cuiot.dmp.lease.enums;

import lombok.Getter;

import java.util.Objects;


/**
 * 订单状态
 *
 * @author
 */
public enum MbRechargeOrderStatus {
    /**
     * 各产品订单状态
     */
    TO_BE_PAY((byte) 1, "待支付"),
    CANCEL((byte) 2, "已取消"),
    PAYING((byte) 3, "支付中"),
    PAY_FAILED((byte) 4, "支付失败"),
    PAID((byte) 5, "已支付"),
    REFUND_APPLY((byte) 6, "退款申请中"),
    REFUNDING((byte) 7, "退款中"),
    REFUND_FAILED((byte) 8, "退款失败"),
    REFUNDED((byte) 9, "已退款"),

    RECHARGING((byte) 21, "充值中"),
    RECHARGING_TO_ACCOUNT((byte) 22, "充值到账"),
    ;
    //能处理支付通知的订单状态
    private static byte[] CAN_PAY_NOTIFY = {TO_BE_PAY.getStatus(), PAYING.getStatus(), PAY_FAILED.getStatus()};


    @Getter
    private byte status;
    @Getter
    private String message;

    MbRechargeOrderStatus(byte status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String parseMessage(Byte st) {
        if (Objects.isNull(st)) {
            return null;
        }
        return parseOrderStatus(st).message;
    }

    public static MbRechargeOrderStatus parseOrderStatus(byte st) {
        for (MbRechargeOrderStatus orderStatus : MbRechargeOrderStatus.values()) {
            if (orderStatus.status == st) {
                return orderStatus;
            }
        }
        return null;
    }

    /**
     * 返回true 曾经支付过
     */
    public static boolean paid(Byte status) {
        return Objects.nonNull(status) && status.compareTo(MbRechargeOrderStatus.PAID.getStatus()) >= 0;
    }
    /**
     * 能处理支付通知的状态
     *
     * @param status
     * @return
     */
    public static boolean canPayNotify(byte status) {
        return checkIndexInArrays(CAN_PAY_NOTIFY, status) > -1;
    }

    public static int checkIndexInArrays(byte[] arrays, Byte key) {
        if (key == null) {
            return -1;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] == key) {
                return i;
            }
        }
        return -1;
    }
}
