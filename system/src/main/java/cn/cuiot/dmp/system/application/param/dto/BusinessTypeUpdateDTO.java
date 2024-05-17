package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/4/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessTypeUpdateDTO extends BusinessTypeCreateDTO {

    private static final long serialVersionUID = -3167121156728639237L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
