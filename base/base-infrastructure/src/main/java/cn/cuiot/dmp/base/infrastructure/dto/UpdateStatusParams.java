package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 状态修改参数
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@Data
public class UpdateStatusParams implements Serializable {
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;

    @NotNull(message = "状态不能为空")
    private Byte status;
}
