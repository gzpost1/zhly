package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/6/6 11:45
 */
@Data
public class AppAssigneeDto {
    @NotNull(message = "任务id不能为空")
    private String taskId;

    /**
     *工单id
     */
    private String processInstanceId;

    /**
     * 转办人
     */
    private List<Long> userIds;
}
