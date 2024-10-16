package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 预缴记录id
 * @Date 2024/10/14 9:45
 * @Created by libo
 */
@Data
public class UpdateChargePayStatusToPaySuccessBYPrePayDto {
    /**
     * 预缴影响记录
     */
    private Integer updateCount;

    /**
     * 账单收款记录id
     */
    private Long chargeReceivedId;
}
