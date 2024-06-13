package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 分页查询
 * @Date 2024/6/12 20:42
 * @Created by libo
 */
@Data
public class ChargeHangupQueryDto extends PageQuery {
    /**
     * 收费id
     */
    @NotNull(message = "收费id不能为空")
    private Long chargeId;
}
