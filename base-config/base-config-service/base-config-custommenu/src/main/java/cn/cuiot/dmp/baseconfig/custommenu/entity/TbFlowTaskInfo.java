package cn.cuiot.dmp.baseconfig.custommenu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统配置-任务配置-任务对象信息
 */
@Data
@TableName(value = "tb_flow_task_info")
public class TbFlowTaskInfo {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 任务对象名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 对象类型
     */
    @TableField(value = "equipment_type")
    private Byte equipmentType;

    /**
     * 任务表单
     */
    @TableField(value = "form_id")
    private Long formId;

    /**
     * 任务配置id
     */
    private Long taskConfigId;

    /**
     * 任务对象选中的id
     */
    private Long taskId;

    /**
     * 序号
     */
    private Integer sort;
}