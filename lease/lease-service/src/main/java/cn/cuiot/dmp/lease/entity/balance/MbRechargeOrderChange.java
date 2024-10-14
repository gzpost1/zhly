package cn.cuiot.dmp.lease.entity.balance;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 充值订单变更记录
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tb_house_recharge_order_change")
public class MbRechargeOrderChange extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 变更id
     */
    @TableId("order_change_id")
    private Long orderChangeId;


    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 变更信息
     */
    private String changeMsg;


    /**
     * 变更时间
     */
    private Date changeTime;


    /**
     * 状态 同订单状态
     */
    private Byte status;


    /**
     * 变更人
     */
    private String changeUser;



}
