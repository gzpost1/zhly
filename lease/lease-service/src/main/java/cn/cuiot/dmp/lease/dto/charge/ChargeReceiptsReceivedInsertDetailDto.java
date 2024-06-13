package cn.cuiot.dmp.lease.dto.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 收款明细
 * @Date 2024/6/13 9:13
 * @Created by libo
 */
@Data
public class ChargeReceiptsReceivedInsertDetailDto {

    /**
     * 应收id
     */
    private Long chargeId;

    /**
     * 实收合计
     */
    private Integer totalReceived = 0;

    /**
     * 违约金应收
     */
    private Integer liquidatedDamagesNeed = 0;

    /**
     * 违约金税率
     */
    private BigDecimal liquidatedDamagesRate;

    /**
     * 欠收合计
     */
    private Integer totalOwe;
}
