package cn.cuiot.dmp.baseconfig.flow.dto.app;

import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import lombok.Data;

import java.util.List;

/**
 * 表单/任务 信息
 * @author pengjian
 * @create 2024/6/4 14:18
 */
@Data
public class ProcessResultDto {

    /**
     * 创建流程时保存的json信息
     */
    private String process;

    /**
     * 用户提交的信息
     */
    private List<CommitProcessEntity> commitProcess;

    /**
     * 当前节点存在的任务数
     */
    private Integer nodeCount;

    /**
     * 流程定义id
     */
    private  String ProcessDefinitionId;

    /**
     * 组织信息
     */
    private List<Long> orgIds;
}
