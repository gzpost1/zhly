package cn.cuiot.dmp.lease.dto.clue;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
public class ClueFinishDTO implements Serializable {

    private static final long serialVersionUID = -6741846222584354185L;

    /**
     * 线索id
     */
    @NotNull(message = "线索id不能为空")
    private Long id;

    /**
     * 线索结果（系统配置自定义）
     */
    @NotNull(message = "线索结果不能为空")
    private Long resultId;

    /**
     * 线索备注
     */
    private String remark;

    /**
     * 完成人ID
     */
    private Long finishUserId;

}
