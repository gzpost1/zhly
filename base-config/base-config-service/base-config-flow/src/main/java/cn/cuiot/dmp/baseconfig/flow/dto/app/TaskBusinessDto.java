package cn.cuiot.dmp.baseconfig.flow.dto.app;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/6 15:09
 */
@Data
public class TaskBusinessDto {

    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    /**
     * 补充说明
     */
    private String comments;

    /**
     * 原因
     */
    private String reason;
}
