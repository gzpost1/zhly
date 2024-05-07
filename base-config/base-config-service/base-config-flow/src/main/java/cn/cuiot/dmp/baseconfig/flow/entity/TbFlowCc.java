package cn.cuiot.dmp.baseconfig.flow.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统配置-流程配置-抄送
 */
@Data
@TableName(value = "tb_flow_cc")
public class TbFlowCc extends YjBaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 流程实例id
     */
    @TableField(value = "process_instance_id")
    private String processInstanceId;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;


    /**
     * 所属组织
     */
    @TableField(value = "org_id")
    private Long orgId;

    /**
     * 节点ID
     */
    private String nodeId;
}