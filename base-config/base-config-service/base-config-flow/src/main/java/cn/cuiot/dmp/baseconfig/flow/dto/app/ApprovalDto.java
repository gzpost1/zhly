package cn.cuiot.dmp.baseconfig.flow.dto.app;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 审批信息
 * @author pengjian
 * @create 2024/6/11 14:57
 */
@Data
public class ApprovalDto {

    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    /**
     * 补充说明
     */
    private String  comments;

    /**
     * 原因
     */
    private String reason;

    /**
     * 节点自选人
     */
    private Map<String, List<UserInfo>> processUsers;
}
