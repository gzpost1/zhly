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
public class FormConfigUpdateDTO extends FormConfigCreateDTO {

    private static final long serialVersionUID = -8848962843800437427L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
