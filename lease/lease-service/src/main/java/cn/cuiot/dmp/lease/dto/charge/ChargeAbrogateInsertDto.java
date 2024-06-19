package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 收费管理-收银台-作废明细
 */
@Data
public class ChargeAbrogateInsertDto {

    /**
     * 作废原因
     */
    @NotBlank(message = "作废原因不能为空")
    private String abrogateDesc;

    /**
     * 数据Id
     */
    @NotNull(message = "数据Id不能为空")
    private Long dataId;
}