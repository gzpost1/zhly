package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * 节点类型
 * @author pengjian
 * @since 2024-05-17
 */
@Data
@TableName("tb_node_type")
public class NodeTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

//
//    /**
//     * 流程实例id
//     */
//    private Long procInstId;


    /**
     * 节点id
     */
    private String nodeId;


    /**
     * 节点类型 APPROVAL审批人 TASK办理人
     */
    private String nodeType;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 处理节点类型
     */
    private String processNodeType;

}
