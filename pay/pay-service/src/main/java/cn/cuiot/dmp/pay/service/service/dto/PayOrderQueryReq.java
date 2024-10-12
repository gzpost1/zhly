package cn.cuiot.dmp.pay.service.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 支付订单查询参数
 *
 * @author huq
 * @ClassName CombineQueryReq
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderQueryReq implements Serializable {

    /**
     * 渠道订单号
     */
    @NotBlank(message = "渠道订单不能为空")
    private String outOrderId;

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long orgId;
}
