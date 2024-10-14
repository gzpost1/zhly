package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.enums.TradeChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 创建订单请求参数
 *
 * @author huq
 * @ClassName CombineCreatePay
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq implements Serializable {

    /**
     * 订单总金额上限
     */
    private static final Integer MAX_TOTAL_FEE = 100000;

    /**
     * 渠道订单号
     */
    @NotBlank(message = "渠道订单号不能为空")
    private String outOrderId;

    /**
     * 支付渠道
     * 0：银联
     * 1：微信
     * 2：支付宝
     * 3:工行
     * 4：广州银联网付通
     */
    @NotNull(message = "支付渠道不能为空")
    private Integer payChannel;

    /**
     * 商户模式
     */
    private Byte mchType;

    /**
     * appId：小程序下单必填
     */
    private String appId;
    /**
     * 交易渠道：目前只支持小程序、公众号
     * 11：云闪付H5支付（云闪付环境内H5）
     * 10：支付宝H5支付（支付宝环境内H5）
     * 03：公众号支付
     * 04：H5支付
     * 05：APP支付
     * 06：native支付
     * 01：小程序支付
     */
    private String tradeType;

    /**
     * 支付方式：
     01:工银e支付 02:微信支付 03:支付宝 04:预付卡 05:转账 06:二维码主扫支付 07:POS支付 08:e支 付有协议小额免密 09:微信线下支付 10:会员卡小额免密 11:支付宝线下支付 12:二维码被扫支付 15:全额优惠 16:融 资支付 17:云闪付 99:其他
     */
    private String payMethod;

    /**
     * 用户openId（小程序、公众号支付必填）
     * 支付宝用户id也传该字段
     */
    private String openId;

    /**
     * 企业id
     */
    private Long orgId;

    /**
     * 支付总金额
     */
    @NotNull(message = "支付总金额不能为空")
    @Min(value = 1, message = "支付总金额至少大于0")
    @Max(value = 2000000000, message = "支付总金额最多2000000000")
    private Integer totalFee;
    /**
     * 终端IP
     */
    private String spbillCreateIp;

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
     * 商户支付号
     */
    private String payMchId;


    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 1:账单缴费
     * 2：预缴
     * 微信支付时不能为空
     */
    private Byte businessType;

    /**
     * 数据类型 0账单 1押金
     */
    private Byte dataType;

    /**
     * 支付校验
     */
    public void validData() {
        // 订单总金额不能超过10,0000,子订单总金额必须等于订单总金额
        if (this.totalFee.compareTo(MAX_TOTAL_FEE) > 0) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "已超过订单金额上限（￥100,000）");
        }

        if (TradeChannelEnum.MINI_APP.getType().equals(this.tradeType)) {
            AssertUtil.notBlank(this.appId, "小程序支付情况下，appId必传");
            AssertUtil.notBlank(this.openId, "小程序支付情况下，openId必传");
            AssertUtil.isFalse(Objects.isNull(this.businessType), "小程序支付情况下，businessType必传");
        }

        if (PayChannelEnum.BALANCE.getPayChannel().equals(this.getPayChannel())) {
            AssertUtil.isFalse(Objects.isNull(this.dataType), "余额支付情况下，dataType必传");
        }
    }


}
