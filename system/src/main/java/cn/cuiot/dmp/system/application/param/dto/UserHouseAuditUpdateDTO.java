package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserHouseAuditUpdateDTO extends UserHouseAuditCreateDTO {

    private static final long serialVersionUID = -2624509342201204744L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
