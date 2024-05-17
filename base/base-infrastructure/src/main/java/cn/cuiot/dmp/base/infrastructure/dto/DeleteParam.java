package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 删除ID
 * @Date 2024/4/23 16:39
 * @Created by libo
 */
@Data
public class DeleteParam implements Serializable {
    @NotNull(message = "ID不能为空")
    private Long id;
}
