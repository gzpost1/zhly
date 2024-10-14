package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 预缴扣费查询
 * @Date 2024/10/14 9:24
 * @Created by libo
 */
@Data
public class PrePayAmountAndHouseId {
    /**
     * 缴费金额
     */
    private Integer amount;
    /**
     * 房号id
     */
    private Long houseId;
}
