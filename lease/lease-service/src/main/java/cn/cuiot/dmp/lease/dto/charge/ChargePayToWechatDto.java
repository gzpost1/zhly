package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 支付中台交付参数
 * @Date 2024/10/10 19:27
 * @Created by libo
 */
@Data
public class ChargePayToWechatDto {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 总金额
     */
    private Integer totalAmount;

    /**
     * 数据类型 0房屋收费 1房屋押金
     */
    private Byte dataType;
}
