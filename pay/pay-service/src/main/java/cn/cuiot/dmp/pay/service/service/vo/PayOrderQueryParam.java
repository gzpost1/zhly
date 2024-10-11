package cn.cuiot.dmp.pay.service.service.vo;

import cn.cuiot.dmp.pay.service.service.entity.PayOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付订单查询参数
 *
 * @author huq
 * @ClassName PayOrderQueryBO
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderQueryParam implements Serializable {

    /**
     * 平台父订单号
     */
    private Long orderId;
    /**
     * 交易时间
     */
    private Date payTime;
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


    /**
     * 普通商户号/特约商户号/二级商户号
     */
    private String payMchId;

    public static PayOrderQueryParam initPayOrderQueryParam(Long orderId, PayOrderEntity orderEntity, String payMchId) {
        return PayOrderQueryParam.builder()
                .orderId(orderId)
                .payTime(orderEntity.getCreateTime())
                .tradeType(orderEntity.getTradeType())
                .payMchId(payMchId)
                .build();
    }
}
