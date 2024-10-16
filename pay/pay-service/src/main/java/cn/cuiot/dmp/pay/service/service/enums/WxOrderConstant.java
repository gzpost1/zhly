package cn.cuiot.dmp.pay.service.service.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信主要状态枚举
 *
 * @author huq
 * @ClassName WxOrderConstant
 * @Date 2022/5/24 14:24
 **/
public class WxOrderConstant {

    public static final String DEFAULT_REFUND_REASON = "申请退款";

    /**
     * 微信支付状态
     */
    public enum WePayOrderStatus {
        NOTPAY("NOTPAY", "未支付", OrderStatusEnum.TO_BE_PAY.getStatus()),
        PAYING("USERPAYING", "支付中", OrderStatusEnum.PAYING.getStatus()),
        SUCCESS("SUCCESS", "支付成功", OrderStatusEnum.PAID.getStatus()),
        PAYERROR("PAYERROR", "支付失败", OrderStatusEnum.PAY_FAILED.getStatus()),
        CLOSED("CLOSED", "已关闭", OrderStatusEnum.CANCEL.getStatus()),
        REFUND("REFUND", "转入退款", OrderStatusEnum.PAID.getStatus()),
        UNKNOWN("", "未知", OrderStatusEnum.UNKNOWN.getStatus()),
        ;
        @Getter
        private String status;
        @Getter
        private String name;
        @Getter
        private Byte yjStatus;

        WePayOrderStatus(String status, String name, Byte yjStatus) {
            this.status = status;
            this.name = name;
            this.yjStatus = yjStatus;
        }

        public static Byte getYjStatus(String status) {
            if (StringUtils.isEmpty(status)) {
                return UNKNOWN.getYjStatus();
            }
            for (WePayOrderStatus orderStatus : WePayOrderStatus.values()) {
                if (status.equals(orderStatus.getStatus())) {
                    return orderStatus.getYjStatus();
                }
            }
            return UNKNOWN.getYjStatus();
        }

    }




}
