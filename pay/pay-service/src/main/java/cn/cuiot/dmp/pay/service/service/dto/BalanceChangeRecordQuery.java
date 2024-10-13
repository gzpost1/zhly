package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author wuyongchong
 * @since 2023-11-29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeRecordQuery extends PageQuery {
    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 变更类型：1-余额支付、2-余额充值、3-兑换余额、4-消费余额、5-退款-退回余额
     */
    private Byte changeType;
    /**
     * 退款单号-退款时存在，跟订单号分开
     */
    private String outRefundNo;

}
