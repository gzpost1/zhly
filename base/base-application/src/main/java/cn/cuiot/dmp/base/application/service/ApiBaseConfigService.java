package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;

/**
 * 流程
 *
 * @Author: zc
 * @Date: 2024-11-12
 */
public interface ApiBaseConfigService {

    /**
     * 初始化企业同步任务配置
     */
    void syncFlowTaskConfig(SyncCompanyDTO dto);

    /**
     * 初始化企业同步流程配置
     */
    void syncFlowConfig(SyncCompanyDTO dto);
}
