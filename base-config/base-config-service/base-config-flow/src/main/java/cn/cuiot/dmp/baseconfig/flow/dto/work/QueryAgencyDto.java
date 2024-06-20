package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

/**
 * 查询我的代办数量
 * @author pengjian
 * @create 2024/6/20 9:37
 */
@Data
public class QueryAgencyDto {

    /**
     * 审批人id
     */
    private Long assignee;

    /**
     * 节点类型
     */
    private String  nodeType;
}
