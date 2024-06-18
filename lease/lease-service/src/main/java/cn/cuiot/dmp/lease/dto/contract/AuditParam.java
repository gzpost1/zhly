package cn.cuiot.dmp.lease.dto.contract;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 审核入参
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class AuditParam implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 审核 2.审核通过 3.审核不通过
     */
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String remark;

    /**
     * 1.提交审核 2.签约审核 3.退订审核 4.作废审核
     */
    private Integer auditType;


}
