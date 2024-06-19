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
     * 节点类型
     */
    private List<String> processNodeType;
}
