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
public class CommonOptionUpdateDTO extends CommonOptionCreateDTO {

    private static final long serialVersionUID = -504315633031610786L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
