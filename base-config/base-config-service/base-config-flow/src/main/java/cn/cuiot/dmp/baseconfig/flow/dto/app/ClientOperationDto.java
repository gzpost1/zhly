package cn.cuiot.dmp.baseconfig.flow.dto.app;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/6 10:33
 */
@Data
public class ClientOperationDto {

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 补充说明
     */
    private String comments;

    /**
     * 原因
     */
    private String reason;

    /**
     * 工单id
     */
    @NotNull(message = "工单id不能为空")
    private Long processInstanceId;
}
