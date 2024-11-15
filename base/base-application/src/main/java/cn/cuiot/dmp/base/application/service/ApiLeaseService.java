package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;

/**
 * 收费管理
 * @Author: zc
 * @Date: 2024-11-12
 */
public interface ApiLeaseService {

    /**
     * 初始化企业同步收费标准
     */
    void syncChargeStandard(SyncCompanyDTO dto);
}
