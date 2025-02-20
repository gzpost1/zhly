package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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

    private Long id;
    /**
     * 房屋id
     */
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

    private List<Byte> changeTypes;

    /**
     * 退款单号-退款时存在，跟订单号分开
     */
    private String outRefundNo;

    /**
     * 开始时间
     */
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    private LocalDate endDate;


}
