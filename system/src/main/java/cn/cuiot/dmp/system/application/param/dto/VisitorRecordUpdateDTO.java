package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorRecordUpdateDTO extends VisitorRecordCreateDTO {

    private static final long serialVersionUID = -948931831553890979L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
