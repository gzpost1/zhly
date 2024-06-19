package cn.cuiot.dmp.lease.dto.clue;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
public class ClueBatchUpdateDTO implements Serializable {

    private static final long serialVersionUID = -1488172628316707150L;

    /**
     * 线索ID列表
     */
    @NotEmpty(message = "线索ID列表")
    private List<Long> idList;

    /**
     * 当前跟进人ID
     */
    private Long currentFollowerId;

    /**
     * 线索结果（系统配置自定义）
     */
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
