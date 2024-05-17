package cn.cuiot.dmp.baseconfig.custommenu.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统配置-任务配置
 */
@Data
@TableName(value = "tb_flow_task_config")
public class TbFlowTaskConfig extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 流程名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 业务分类
     */
    @TableField(value = "business_type_id")
    private Long businessTypeId;

    /**
     * 任务描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 状态 0停用 1启用
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

}