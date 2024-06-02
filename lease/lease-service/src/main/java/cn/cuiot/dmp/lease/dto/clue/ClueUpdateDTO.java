package cn.cuiot.dmp.lease.dto.clue;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClueUpdateDTO extends ClueCreateDTO {

    private static final long serialVersionUID = 6066359296979829104L;

    /**
     * 线索id
     */
    @NotNull(message = "线索id不能为空")
    private Long id;

}
