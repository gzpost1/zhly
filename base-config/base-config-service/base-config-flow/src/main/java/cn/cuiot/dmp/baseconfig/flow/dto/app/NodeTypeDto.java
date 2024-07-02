package cn.cuiot.dmp.baseconfig.flow.dto.app;

import lombok.Data;

import java.util.List;

/**
 * 节点类型
 * @author pengjian
 * @create 2024/6/4 14:59
 */
@Data
public class NodeTypeDto {

    /**
     * 节点名称
     */
    private String processNodeName;

    /**
     * 子节点id
     */
    private String processNodeId;
}
