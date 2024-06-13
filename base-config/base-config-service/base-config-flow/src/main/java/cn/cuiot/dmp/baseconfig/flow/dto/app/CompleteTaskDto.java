package cn.cuiot.dmp.baseconfig.flow.dto.app;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @create 2024/6/5 17:13
 */
@Data
public class CompleteTaskDto {

    /**
     * 表单数据
     */
    private CommitProcessEntity commitProcess;

    /**
     * 上一节点自选 key 是nodeId, value是用户信息
     */
    private Map<String, List<UserInfo>> processUsers;

    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    /**
     * 完成比例
     */
    private String completionRatio;
}
