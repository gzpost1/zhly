package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * 收费管理-收费项目Dto
 *
 * @author zc
 */
@Data
public class ChargeChargeItemDto {
    /**
     * 业务id
     */
    private Long dataId;

    /**
     * 收费项目名称
     */
    private String chargeItemName;
}