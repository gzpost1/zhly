package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomConfigUpdateDTO extends CustomConfigCreateDTO {

    private static final long serialVersionUID = 1102089407312853212L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
