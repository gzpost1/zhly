package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author xulei
 * @create 2024-10-11 17:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaySuccessVO {


    /**
     * 渠道订单号
     */
    private String outOrderId;

    /**
     * 微信交易单号
     */
    private String transactionNo;

    /**
     * 支付单状态
     * 1：待支付
     * 2：已取消
     * 3：支付中
     * 4：支付失败
     * 5：已支付
     * 99：未知状态
     */
    private Byte status;

    /**
     * 1:账单缴费
     * 2：预缴
     */
    private Byte businessType;

    /**
     * 支付手续费费率
     */
    private BigDecimal payRate;
}
