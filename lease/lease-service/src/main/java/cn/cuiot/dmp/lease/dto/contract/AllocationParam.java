package cn.cuiot.dmp.lease.dto.contract;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class AllocationParam implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "分配人ID不能为空")
    private Long followUpId;
    @NotNull(message = "分配人姓名不能为空")
    private String followUpName;

}
