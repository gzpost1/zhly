package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/4/25 11:34
 */
@Data
public class FirstFormDto {

    /**
     * 流程定义id
     */
    private String  processDefinitionId;

    /**
     * 流程定义的key
     */
    private String taskDefinitionKey;
}
