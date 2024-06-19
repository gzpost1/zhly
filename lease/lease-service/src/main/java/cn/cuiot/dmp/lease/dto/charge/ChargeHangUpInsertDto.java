package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 收费管理-收银台-作废明细
 */
@Data
public class ChargeHangUpInsertDto {

    /**
     * 挂起/解挂原因
     */
    @NotBlank(message = "原因不能为空")
    private String hangupDesc;

    /**
     * 数据Id
     */
    @NotNull(message = "数据Id不能为空")
    private Long dataId;
}