package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 房屋账单查询
 * @Date 2024/10/10 17:23
 * @Created by libo
 */
@Data
public class AppChargemanagerQuery extends PageQuery {
    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;

    /**
     * 未缴0 已缴1
     */
    @NotNull(message = "查询类型不能为空")
    private Byte receivableStatus;
}
