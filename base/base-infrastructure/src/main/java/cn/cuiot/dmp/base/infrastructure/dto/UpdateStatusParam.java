package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 状态修改参数
 * @Date 2024/4/23 16:40
 * @Created by libo
 */
@Data
public class UpdateStatusParam implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "状态不能为空")
    private Byte status;
}
