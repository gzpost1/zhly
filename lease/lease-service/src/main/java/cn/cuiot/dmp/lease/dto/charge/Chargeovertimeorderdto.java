package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 超时支付取消的订单
 * @Date 2024/10/11 9:46
 * @Created by libo
 */
@Data
public class Chargeovertimeorderdto {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 数据id
     */
    private Long dataId;
}
