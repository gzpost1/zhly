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
     * 退回传
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
    private Long processInstanceId;
}
