package cn.cuiot.dmp.lease.dto.charge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 支付详细参数
 * @Date 2024/10/10 19:28
 * @Created by libo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargePayToWechatDetailDto implements Serializable {
    /**
     * 应收账目id
     */
    private Long chargeId;

    /**
     * 应收账目金额
     */
    private Integer chargeAmount;

    /**
     * 企业ID
     */
    private Long companyId;
}
