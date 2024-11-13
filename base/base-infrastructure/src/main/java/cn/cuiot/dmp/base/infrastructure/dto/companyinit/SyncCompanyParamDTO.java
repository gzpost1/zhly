package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 初始化企业参数
 *
 * @Author: zc
 * @Date: 2024-11-13
 */
@Data
public class SyncCompanyParamDTO implements Serializable {

    /**
     * 目标企业
     */
    private Long targetCompanyId;

    /**
     * 组织部门
     */
    private DepartmentDto departmentInfo;

    /**
     * 表单
     */
    private Map<Long, FormConfigSyncDTO> formConfigMap;

    /**
     * 任务配置
     */
    private Map<Long, Long> taskConfigMap;

    /**
     * 任务信息详情
     */
    private Map<Long, FormConfigDetailSyncDTO> formConfigDetailMap;

    /**
     * 任务对象信息
     */
    private Map<Long, FlowTaskInfoSyncDTO> taskInfoMap;

    /**
     * 常用选项->系统选项
     */
    private Map<Long, CustomConfigDetailSyncDTO> customConfigMap;
}
