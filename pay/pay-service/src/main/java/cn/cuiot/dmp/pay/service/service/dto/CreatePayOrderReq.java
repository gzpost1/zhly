package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.pay.service.service.enums.TradeChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static cn.cuiot.dmp.pay.service.service.enums.AppPayMethodEnum.APP_PAY_METHOD_LSIT;


/**
 * 预下单支付请求参数
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayOrderReq implements Serializable {

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private Long orderId;
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
     * 交易渠道：
     * 01：小程序支付
     * 03：公众号支付
     * 04：H5支付
     * 05：APP支付
     * 06：native支付
     * 09:二维码主扫支付（订 单）
     */
    @NotBlank(message = "交易渠道不能为空")
    private String tradeType;

    /**
     * appId：小程序下单必填
     */
    private String appId;

    /**
     * 用户openId（小程序、公众号支付必填）
     * 支付宝用户id也传该字段
     */
    private String openId;

    /**
     * app或H5支付环境下的交易渠道
     * 02：微信支付
     * 17：云闪付
     * 03：支付宝
     * 广州银联支付新增字段
     */
    private String appPayMethod;

    /**
     * 支付校验
     */
    public void validData() {
        AssertUtil.isFalse(Objects.isNull(this.payChannel) || Objects.isNull(this.orderId), "支付渠道或者订单号为空");
        if (TradeChannelEnum.MINI_APP.getType().equals(this.tradeType)) {
            AssertUtil.notBlank(this.appId, "小程序支付情况下，appId必传");
            AssertUtil.notBlank(this.openId, "小程序支付情况下，openId必传");
        }
        if (TradeChannelEnum.APP.getType().equals(this.tradeType)) {
            AssertUtil.notBlank(this.appPayMethod, "APP支付环境下，appPayMethod必传");
            if (!Arrays.stream(APP_PAY_METHOD_LSIT).anyMatch(p -> p.equals(this.appPayMethod))) {
                throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, " app支付环境下的交易渠道上送不正确");
            }
        }
    }

}
