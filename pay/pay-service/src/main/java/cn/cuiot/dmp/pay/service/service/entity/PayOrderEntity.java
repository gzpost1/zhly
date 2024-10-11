package cn.cuiot.dmp.pay.service.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.pay.service.service.config.NormalWeChatConfig;
import cn.cuiot.dmp.pay.service.service.dto.CreateOrderReq;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 支付订单表
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_pay_order")
public class PayOrderEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("order_id")
    private Long orderId;


    /**
     * 渠道订单号-唯一
     */
    private String outOrderId;


    /**
     * 支付商户号
     */
    private String payMchId;


    /**
     * 支付渠道:0-银联，1-微信,2-支付宝,3-工行
     */
    private Integer payChannel;


    /**
     * 0-普通服务商模式，1-普通商户模式，2-电商收付通模式
     */
    private Byte mchType;


    /**
     * 支付方式：
        01:工银e支付 02:微信支付 03:支付宝 04:预付卡 05:转账 06:二维码主扫支付 07:POS支付 08:e支 付有协议小额免密 09:微信线下支付 10:会员卡小额免密 11:支付宝线下支付 12:二维码被扫支付 15:全额优惠 16:融 资支付 17:云闪付 99:其他
     */
    private String payMethod;


    /**
     * 支付状态：
        1：待支付
        2：已取消
        3：支付中
        4：支付失败
        5：已支付
        99：其他
     */
    private Byte status;


    /**
     * 支付总金额，单位分
     */
    private Integer totalFee;


    /**
     * 支付成功时间
     */
    private Date payCompleteTime;


    /**
     * 备注
     */
    private String remark;


    /**
     * 交易IP:支付提交的用户的IP地址
     */
    private String spbillCreateIp;


    /**
     * 交易渠道：
        03：H5支付
        04：公众号支付
        05：APP支付
        06：native支付
        13：小程序支付

     */
    private String tradeType;


    /**
     * 小程序appId
     */
    private String appId;


    /**
     * 第三方用户标识
     */
    private String openId;


    /**
     * 账单请求数据
     */
    private String payReqJson;


    /**
     * 预付单接口返回数据
     */
    private String payResultJson;


    /**
     * 支付手续费
     */
    private Integer payCharge;


    /**
     * 主单初始化
     */
    public static PayOrderEntity initOrderEntity(CreateOrderReq param, NormalWeChatConfig weChatConfig) {
        PayOrderEntity orderEntity = BeanMapper.map(param, PayOrderEntity.class);
        orderEntity.setOutOrderId(param.getOutOrderId());
        orderEntity.setCreateTime(new Date());
        //因为是直接下预付单所以这里是支付中状态
        orderEntity.setStatus(OrderStatusEnum.TO_BE_PAY.getStatus());
        orderEntity.setOrderId(IdWorker.getId());
        orderEntity.setPayMchId(Optional.ofNullable(param).map(CreateOrderReq::getSubOrderItems).map(u ->u.get(0).getPayMchId()).orElse(weChatConfig.getPayMchId()));
        return orderEntity;
    }
}
