package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 余额变动任务参数
 *
 * @author huq
 * @ClassName MbBalanceEventDto
 * @Date 2023/11/17 14:44
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceEventAggregate implements Serializable {
    /**
     * 订单id-用户充值的订单号
     */
    private String orderId;
    /**
     * 订单名称
     */
    private String orderName;
    /**
     * 变动的余额
     */
    private Integer balance;
    /**
     * 任务完成时间-不能为空
     */
    private Date createTime;
    /**
     * 变更类型：1-余额支付、2-余额充值、3-兑换余额、4-平台操作、5-退款-退回余额
     */
    private Byte changeType;
    /**
     * 余额变更明细表id-退款时存在(发通知给订单时使用，本身余额变更业务不使用）
     */
    private Long balanceReportId;
    /**
     * 订单号-余额支付时的订单号
     */
    private String outTradeNo;
    /**
     * 之前的变更类型：1-余额支付、2-余额充值、3-兑换余额、4-平台操作、5-退款-退回余额
     * 退款时存在，跟orderId确定之前唯一的消费或充值记录
     */
    private Byte beforeChangeType;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 父退款单号
     */
    private String outRefundNo;

    /**
     * 数据类型 0账单 1押金
     */
    private Byte dateType;

}
