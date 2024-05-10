package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程配置-所属组织中间表
 */
@Data
@TableName(value = "tb_flow_config_org",autoResultMap = true)
public class TbFlowConfigOrg {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 流程配置ID
     */
    @TableField(value = "flow_config_id")
    private Long flowConfigId;

    /**
     * 组织ID
     */
    @TableField(value = "org_id")
    private Long orgId;
}