package cn.cuiot.dmp.pay.service.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付请求参数
 *
 * @author huq
 * @ClassName CombineCreatePay
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderAttach implements Serializable {

    /**
     * 支付手续费费率
     */
    private BigDecimal payRate;

    /**
     * 1:账单缴费
     * 2：预缴
     */
    private Byte businessType;


}
