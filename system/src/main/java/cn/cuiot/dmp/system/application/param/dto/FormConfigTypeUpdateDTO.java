package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormConfigTypeUpdateDTO extends FormConfigTypeCreateDTO {

    private static final long serialVersionUID = -5064340312990568623L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
