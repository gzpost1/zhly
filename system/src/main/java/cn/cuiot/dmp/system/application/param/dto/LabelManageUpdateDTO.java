package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LabelManageUpdateDTO extends LabelManageCreateDTO{

    private static final long serialVersionUID = 2602122766859347063L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
