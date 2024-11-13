package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置-任务配置
 *
 * @Author: zc
 * @Date: 2024-11-13
 */
@Data
public class FlowTaskConfigSyncDTO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 业务分类
     */
    private Long businessTypeId;

    /**
     * 任务描述
     */
    private String remark;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    /**
     * 企业ID
     */
    private Long companyId;
}