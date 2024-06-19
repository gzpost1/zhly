package cn.cuiot.dmp.lease.dto.clue;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/3
 */
@Data
public class ClueDistributeDTO implements Serializable {

    private static final long serialVersionUID = 5696803714048307097L;

    /**
     * 线索id
     */
    @NotNull(message = "线索id不能为空")
    private Long id;

    /**
     * 当前跟进人ID
     */
    @NotNull(message = "当前跟进人ID不能为空")
    private Long currentFollowerId;

}
