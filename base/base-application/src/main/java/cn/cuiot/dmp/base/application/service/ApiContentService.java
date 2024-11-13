package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;

/**
 * 小程序配置
 *
 * @Author: zc
 * @Date: 2024-11-12
 */
public interface ApiContentService {

    /**
     * 初始化企业同步小程序配置
     */
    void syncData(SyncCompanyDTO dto);

    /**
     * 清空同步数据
     */
    void cleanSyncData(SyncCompanyDTO dto);
}
