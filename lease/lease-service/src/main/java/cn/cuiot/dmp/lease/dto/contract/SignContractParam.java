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
public class SignContractParam extends AuditParam implements Serializable {

    /**
     * 租赁合同 ID
     */
    @NotNull(message = "租赁合同不能为空")
    private Long contractLeaseId;

}
