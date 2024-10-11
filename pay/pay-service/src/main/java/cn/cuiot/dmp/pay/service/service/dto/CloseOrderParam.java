package cn.cuiot.dmp.pay.service.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单关闭参数
 *
 * @author huq
 * @ClassName CombineCreatePay
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderParam implements Serializable {


    /**
     * 平台支付父订单号
     */
    private String orderId;
    /**
     * 支付渠道父订单号
     */
    private String payOrderId;
    /**
     * 交易时间
     */
    private Date payTime;
    /**
     * 子单信息
     */
    private List<CloseSubOrderItem> subOrderItems;
    /**
     * 交易渠道：
     * 03：公众号支付
     * 04：H5支付
     * 05：APP支付
     * 06：native支付
     * 01：小程序支付
     * 09:二维码主扫支付（订 单）
     */
    private String tradeType;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CloseSubOrderItem implements Serializable {
        /**
         * 子单订单号
         */
        private Long subOrderId;

        /**
         * 二级商户支付号
         */
        private String payMchId;


    }

}
