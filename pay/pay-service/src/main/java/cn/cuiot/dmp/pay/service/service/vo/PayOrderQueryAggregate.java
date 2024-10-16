package cn.cuiot.dmp.pay.service.service.vo;

import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.PromotionDetailResp;
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
 * @ClassName PayOrderQueryBO
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderQueryAggregate implements Serializable {
    /**
     * 支付父订单号
     */
    private String outOrderId;

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
     * 支付方状态
     */
    private String thirdStatus;
    /**
     * 支付总金额
     */
    private Integer totalFee;
    /**
     * 支付方父订单号
     */
    private String payOrderId;
    /**
     * 备注字段
     */
    private String remark;
    /**
     * 支付成功时间
     * yyyy-MM-dd HH:mm:ss
     */
    private Date payCompleteTime;

    /**
     * 支付号
     */
    private String payMchId;

    /**
     * 微信优惠信息
     */
    private List<PromotionDetailResp> promotionDetail;

    /**
     * 优惠金额
     */
    private Integer discountAmount;

    /**
     * 商品简单描述。需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
     * 对应以前的body字段
     * 不能超过20字符
     */
    private String productName;

    private String attach;

}
