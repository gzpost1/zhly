package cn.cuiot.dmp.baseconfig.custommenu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 任务配置组织机构ID
 */
@Data
@TableName(value = "tb_flow_task_org")
public class TbFlowTaskOrg {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 流程配置任务ID
     */
    @TableField(value = "flow_task_id")
    private Long flowTaskId;

    /**
     * 组织机构ID
     */
    @TableField(value = "org_id")
    private Long orgId;
}