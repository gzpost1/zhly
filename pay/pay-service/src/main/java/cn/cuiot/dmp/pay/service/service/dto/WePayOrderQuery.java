package cn.cuiot.dmp.pay.service.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huq
 * @ClassName PayOrderQuery
 * @Date 2021/7/26 9:47
 **/
@Data
public class WePayOrderQuery implements Serializable {

    /**
     * 平台大订单号
     */
    private String outTradeNo;
    /**
     * 支付订单号
     */
    private String transactionId;
    /**
     * 支付商户号（普通商户必填）
     */
    private String payMchId;
    /**
     * 平台子订单号
     */
    private String subOutTradeNo;
}
