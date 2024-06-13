package cn.cuiot.dmp.lease.dto.clue;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClueRecordPageQueryDTO extends PageQuery {

    private static final long serialVersionUID = 5330801664434241037L;

    /**
     * 线索ID
     */
    private Long clueId;

}
