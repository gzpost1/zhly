package cn.cuiot.dmp.baseconfig.flow.dto.app.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 查询用户提交的信息
 * @author pengjian
 * @create 2024/6/5 15:46
 */
@Data
public class UserSubmitDataDto {

    /**
     * 工单id
     */
    @NotNull(message = "工单id不能为空")
    private Long procInstId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 节点操作信息id
     */
    private String businessTypeId;
}
