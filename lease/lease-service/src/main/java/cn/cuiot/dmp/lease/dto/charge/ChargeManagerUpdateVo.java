package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收费管理-收银台-缴费管理
 */
@Data
public class ChargeManagerUpdateVo {

    /**
     * 收费Id
     */
    private Long chargeId;

    /**
     * 页面保存使用违约金应收
     */
    private Integer pageLiquidatedDamagesNeed;

    /**
     * 页面保存使用违约金税率
     */
    private BigDecimal pageLiquidatedDamagesRate;
}