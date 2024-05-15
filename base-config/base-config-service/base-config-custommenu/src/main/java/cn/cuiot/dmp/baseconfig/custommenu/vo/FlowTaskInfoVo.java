package cn.cuiot.dmp.baseconfig.custommenu.vo;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FormObjectOperates;
import lombok.Data;

/**
 * 系统配置-任务配置-任务对象信息
 */
@Data
public class FlowTaskInfoVo {

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

    /**
     * 对象表单
     */
    private FormObjectOperates formOperates;
}