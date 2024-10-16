package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付订单查询参数
 *
 * @author huq
 * @ClassName PayOrderQueryBO
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderQueryParam implements Serializable {

    /**
     * 渠道订单号
     */
    private String outOrderId;


    /**
     * 普通商户号/特约商户号/二级商户号
     */
    private String payMchId;

    /**
     * 企业id
     */
    private Long orgId;

    public static PayOrderQueryParam initPayOrderQueryParam(String outOrderId, String payMchId,Long orgId) {
        return PayOrderQueryParam.builder()
                .outOrderId(outOrderId)
                .payMchId(payMchId)
                .orgId(orgId)
                .build();
    }
}
