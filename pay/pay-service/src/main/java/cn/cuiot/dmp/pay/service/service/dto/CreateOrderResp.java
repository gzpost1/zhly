package cn.cuiot.dmp.pay.service.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付返回
 *
 * @author huq
 * @ClassName CreateOrderResp
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResp implements Serializable {

    /**
     * 平台支付父订单号
     */
    private String orderId;

    /**
     * 渠道支付订单号
     */
    private String outOrderId;

    /**
     * 支付签名：微信有，支付宝没有
     */
    private UnifiedOrderSignRsp unifiedOrderSignRsp;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnifiedOrderSignRsp {
        String appId;
        String timeStamp;
        String nonceStr;

        @JsonProperty("package")
        String pkg;

        String signType;

        String paySign;
    }
}
