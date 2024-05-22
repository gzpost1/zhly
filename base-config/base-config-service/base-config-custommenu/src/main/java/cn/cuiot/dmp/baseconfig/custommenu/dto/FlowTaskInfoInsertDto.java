package cn.cuiot.dmp.baseconfig.custommenu.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 系统配置-任务配置-任务对象信息
 */
@Data
public class FlowTaskInfoInsertDto {


    /**
     * 任务对象名称
     */
    private String name;

    /**
     * 对象类型 0楼盘 1楼栋 2房屋 3空间 4设备 5车位 6自定义
     */
    private Byte equipmentType;

    /**
     * 任务表单
     */
    @NotNull(message = "任务表单不能为空")
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