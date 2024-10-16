package cn.cuiot.dmp.lease.enums;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

/**
 * @Author: lqj
 * @Date: 2020/9/2
 */
public enum PayStatus {
    /**
     * 1-待支付, 2-已取消, 3-支付中, 4-支付失败, 5-已支付, 7-退款中, 8-退款失败,9-已退款，32-仅退款
     */
    P_TO_BE_PAY((byte) 1, "待支付"),
    P_CANCEL((byte) 2, "已取消"),
    P_PAYING((byte) 3, "支付中"),
    P_PAY_FAILED((byte) 4, "支付失败"),
    P_PAID((byte) 5, "已支付"),
    P_REFUNDING((byte) 7, "退款中"),
    P_REFUND_FAILED((byte) 8, "退款失败"),
    P_REFUNDED((byte) 9, "已退款"),
    ONLY_REFUND((byte) 32, "仅退款"),
    ;
    //后台操作可退的订单状态
    public static byte[] BACK_CAN_REFUND_STATUS = {P_PAID.getStatus(), P_REFUND_FAILED.getStatus(), ONLY_REFUND.getStatus()};
    /**
     * 从未支付过
     */
    public static Set<Byte> NOT_PAID = Sets.newHashSet(P_TO_BE_PAY.getStatus(), P_CANCEL.getStatus(), P_PAYING.getStatus(), P_PAY_FAILED.getStatus());
    @Getter
    private byte status;
    @Getter
    private String message;

    PayStatus(byte status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getMessage(Byte status) {
        if (status == null) {
            return null;
        }
        for (PayStatus payStatus : PayStatus.values()) {
            if (payStatus.getStatus() == status) {
                return payStatus.getMessage();
            }
        }
        return null;
    }

    /**
     * 状态可以改变
     */
    public static boolean canChangeStatus(Byte st) {
        return !ObjectUtil.equal(P_REFUNDED.getStatus(), st)
                && !ObjectUtil.equal(ONLY_REFUND.getStatus(), st);
    }

    /**
     * 不需要调用支付中台退
     */
    public static boolean notCanInvokeRefund(Byte st) {
        return ObjectUtil.equal(P_REFUNDED.getStatus(), st)
                || ObjectUtil.equal(ONLY_REFUND.getStatus(), st);
    }

    /**
     * 未产生过支付
     */
    public static boolean notPaid(Byte st) {
        return Objects.nonNull(st) && NOT_PAID.contains(st);
    }
}
