package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 支付回调参数
 * @Date 2024/10/10 19:14
 * @Created by libo
 */
@Data
public class ChargePayWechatResultDto {
    /**
     * 支付中台返回id
     */
    private Long payId;

    /**
     * 支付单json
     */
    private String orderJson;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 订单id
     */
    private Long orderId;
}
