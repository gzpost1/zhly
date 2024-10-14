package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.pay.service.service.enums.TradeChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付请求参数
 *
 * @author huq
 * @ClassName CombineCreatePay
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderParam implements Serializable {

    /**
     * appId：小程序下单必填
     */
    private String appId;
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
     * 用户openId（小程序、公众号支付必填）
     * 支付宝用户id也传该字段
     */
    private String openId;
    /**
     * 支付总金额
     */
    private Integer totalFee;

    /**
     * 终端IP
     */
    private String spbillCreateIp;


    /**
     * 渠道订单号
     */
    private String outOrderId;

    /**
     * 企业id
     */
    private Long orgId;

    /**
     * 附加数据（可作为自定义字段使用，查询API和支付通知中原样返回）
     */
    private String attach;

    /**
     * 商户支付号
     */
    private String payMchId;
    /**

     /**
     * 优惠标记
     */
    private String goodsTag;

    /**
     * 商品简单描述。需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
     * 对应以前的body字段
     * 不能超过20字符
     */
    private String productName;

    /**
     * 房屋id（房屋充值时不能为空）
     */
    private Long houseId;

    /**
     * 1:账单缴费
     * 2：预缴
     */
    private Byte businessType;

    /**
     * 数据类型 0账单 1押金
     */
    private Byte dateType;

    public static CreateOrderParam initDate(CreateOrderReq param){
        CreateOrderParam createOrderParam = CreateOrderParam.builder()
                .appId(param.getAppId())
                .tradeType(TradeChannelEnum.getPayCode(param.getTradeType()))
                .openId(param.getOpenId())
                .totalFee(param.getTotalFee())
                .spbillCreateIp(param.getSpbillCreateIp())
                .outOrderId(param.getOutOrderId())
                .orgId(param.getOrgId())
                //微信支付时加上业务类型，用于支付回调时区分是缴费还是预缴
                .attach(String.valueOf(param.getBusinessType()))
                .payMchId(param.getPayMchId())
                .goodsTag(param.getGoodsTag())
                .productName(param.getProductName())
                .houseId(param.getHouseId())
                .businessType(param.getBusinessType())
                .dateType(param.getDateType())
                .build();
        return createOrderParam;
    }

}
