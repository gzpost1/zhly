package cn.cuiot.dmp.lease.vo.balance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 预付单返回
 * @author huq
 * @ClassName MbChargeOrderCreateVo
 * @Date 2023/11/30 14:16
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MbRechargeOrderCreateVo implements Serializable {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 支付参数
     */
    private Object payContent;
}
