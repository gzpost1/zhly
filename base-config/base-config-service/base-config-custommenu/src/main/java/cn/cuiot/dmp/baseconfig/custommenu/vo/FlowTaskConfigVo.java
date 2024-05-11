package cn.cuiot.dmp.baseconfig.custommenu.vo;

import lombok.Data;

import java.util.List;

/**
 * 系统配置-任务配置
 */
@Data
public class FlowTaskConfigVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务配置名称
     */
    private String name;

    /**
     * 业务分类
     */
    private Long businessTypeId;

    /**
     * 所属组织
     */
    private List<Long> orgId;

    /**
     * 任务描述
     */
    private String remark;

    /**
     * 任务对象信息
     */
    private List<FlowTaskInfoVo> taskInfoList;

    /**
     * 任务表单ID
     */
    private List<Long> taskMenuIds;
}