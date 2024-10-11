package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.pay.service.service.entity.PayOrderEntity;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huq
 * @ClassName CombineQueryOrderResp
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderQueryResp implements Serializable {
    /**
     * 支付父订单号
     */
    private Long orderId;
    /**
     * 合单订单号
     */
    private String outOrderId;


    /**
     * 交易渠道
     * 03：公众号支付
     * 04：H5支付
     * 05：APP支付
     * 06：native支付
     * 01：小程序支付
     */
    private String tradeType;
    /**
     * 用户openId（小程序、公众号支付必填）
     */
    private String openId;
    /**
     * 支付渠道
     * 0：银联
     * 1：微信
     * 2：支付宝
     */
    private Integer payChannel;

    /**
     * 商户类型：0-普通服务商模式，1-普通商户模式,2-电商收付通模式
     */
    private Byte mchType;

    /**
     * 支付方式：
     * 01:工银e支付 02:微信支付 03:支付宝 04:预付卡 05:转账 06:二维码主扫支付 07:POS支付 08:e支 付有协议小额免密 09:微信线下支付 10:会员卡小额免密 11:支付宝线下支付
     * 12:二维码被扫支付 15:全额优惠 16:融 资支付 17:云闪付 99:其他
     */
    private String payMethod;
    /**
     * 支付状态：
     * 1：待支付
     * 2：已取消
     * 3：支付中
     * 4：支付失败
     * 5：已支付
     * 99：其他
     */
    private Byte status;
    /**
     * 支付总金额
     */
    private Integer totalFee;
    /**
     * 交易终端IP
     */
    private String spbillCreateIp;
    /**
     * 支付成功时间
     * yyyy-MM-dd HH:mm:ss
     */
    private Date payCompleteTime;
    /**
     * 支付手续费
     */
    private Integer payCharge;
    /**
     * 子单信息
     */
    private List<SubOrderItem> subOrderItems;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubOrderItem implements Serializable {
        /**
         * 支付子订单号
         */
        private Long subOrderId;

        /**
         * 子单订单号
         */
        @Mapping("outSubOrderId")
        private String outTradeNo;
        /**
         * 子单金额，单位分
         */
        private Integer totalFee;

        /**
         * 二级商户支付号
         */
        private String payMchId;

        /**
         * 子单状态
         * 1：待支付
         * 2：已取消
         * 3：支付中
         * 4：支付失败
         * 5：已支付
         * 99：未知状态
         */
        private Byte status;


        /**
         * 附加数据（可作为自定义字段使用，查询API和支付通知中原样返回）
         */
        private String attach;

        /**
         * 商品简单描述。需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
         * 对应以前的body字段
         * 不能超过20字符
         */
        private String productName;

        /**
         * 备注信息
         */
        private String remark;
        /**
         * 支付手续费
         */
        private Integer payCharge;
        /**
         * 支付手续费率
         */
        private BigDecimal payChargeRate;
    }
    public static PayOrderQueryResp initReturn(PayOrderEntity orderEntity) {
        return PayOrderQueryResp.builder()
                .outOrderId(orderEntity.getOutOrderId())
                .openId(orderEntity.getOpenId())
                .orderId(orderEntity.getOrderId())
                .payChannel(orderEntity.getPayChannel())
                .payMethod(orderEntity.getPayMethod())
                .payCompleteTime(orderEntity.getPayCompleteTime())
                .spbillCreateIp(orderEntity.getSpbillCreateIp())
                .status(orderEntity.getStatus())
                .totalFee(orderEntity.getTotalFee())
                .tradeType(orderEntity.getTradeType())
                .build();
    }
}
