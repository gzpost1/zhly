package cn.cuiot.dmp.baseconfig.flow.dto.app.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @create 2024/5/27 16:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingProcessQuery {
    /**
     * 审批人
     */
    private Long assignee;
}
