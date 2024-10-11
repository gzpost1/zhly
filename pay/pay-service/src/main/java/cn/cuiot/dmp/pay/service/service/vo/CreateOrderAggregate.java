package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付返回
 *
 * @author huq
 * @ClassName CreateOrderBO
 * @Date 2021/11/3 14:06
 **/
@Data
public class CreateOrderAggregate implements Serializable {

    /**
     * 平台支付订单号
     */
    private String orderId;
    /**
     * 支付系统交易订单号（微信为空，支付宝返回）
     */
    private String tradeNo;
    /**
     * 支付返回的字符串
     */
    private String content;
    /**
     * 微信：与订单号
     * 支付宝：支付宝交易单号
     */
    private String prepayId;
    /**
     * 交易支付号（广州银联有）
     */
    private String payOrderId;
    /**
     * H5 支付跳转链接，可通过访问 mweb_url 拉起微信/支付宝/
     * 云闪付支付收银台的中间页面
     */
    private String mwebUrl;
    /**
     * 支付签名：微信有，支付宝没有
     */
    private UnifiedOrderSignRsp unifiedOrderSignRsp;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnifiedOrderSignRsp implements Serializable{
        String appId;
        String timeStamp;
        String nonceStr;

        String pkg;

        String signType;

        String paySign;
    }
}
