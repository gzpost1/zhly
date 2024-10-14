package cn.cuiot.dmp.pay.service.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 余额明细变动表
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tb_house_balance_change_record")
public class BalanceChangeRecord extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;


    /**
     * 房屋id
     */
    private Long houseId;


    /**
     * 变动金额
     */
    private Integer balance;


    /**
     * 变更前余额
     */
    private Integer beforeBalance;


    /**
     * 变动原因
     */
    private String reason;


    /**
     * 订单号
     */
    private Long orderId;


    /**
     * 订单名称
     */
    private String orderName;


    /**
     * 余额任务类型1-余额支付、2-用户充值、3-积分兑换、4-平台操作、5-退款-退回余额
     */
    private Byte changeType;

    /**
     * 退款单号-退款时存在，跟订单号分开
     */
    private String outRefundNo;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 数据类型 0账单 1押金
     */
    private Byte dataType;
}
