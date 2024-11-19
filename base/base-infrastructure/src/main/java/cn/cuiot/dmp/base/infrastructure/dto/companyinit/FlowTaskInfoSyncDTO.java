package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置-任务配置-任务对象信息
 * @Author: zc
 * @Date: 2024-11-13
 */
@Data
public class FlowTaskInfoSyncDTO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务对象名称
     */
    private String name;

    /**
     * 对象类型
     */
    private Byte equipmentType;

    /**
     * 任务表单
     */
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
