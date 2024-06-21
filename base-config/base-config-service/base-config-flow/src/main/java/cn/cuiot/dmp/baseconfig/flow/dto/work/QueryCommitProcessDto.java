package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/18 9:13
 */
@Data
public class QueryCommitProcessDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long procInstId;


    /**
     * 用户id
     */
    
    private Long userId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 对象id
     */
    private Long dataId;
}
