package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonOptionTypeUpdateDTO extends CommonOptionTypeCreateDTO {

    private static final long serialVersionUID = 6803914990014225790L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
