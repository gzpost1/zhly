package cn.cuiot.dmp.pay.service.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单关闭参数
 *
 * @author huq
 * @ClassName CombineCreatePay
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderParam implements Serializable {

    private Long orgId;

    /**
     * 渠道订单号
     */
    private String outOrderId;


}
