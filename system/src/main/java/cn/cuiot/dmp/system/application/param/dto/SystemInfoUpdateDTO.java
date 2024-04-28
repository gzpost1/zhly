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
public class SystemInfoUpdateDTO extends SystemInfoCreateDTO {

    private static final long serialVersionUID = -4372933596208195307L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
