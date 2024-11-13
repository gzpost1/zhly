package cn.cuiot.dmp.baseconfig.controller;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.baseconfig.custommenu.service.FlowTaskConfigSyncService;
import cn.cuiot.dmp.baseconfig.flow.service.FlowConfigSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 供其他服务调用接口
 *
 * @Author: zc
 * @Date: 2024-11-07
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private FlowTaskConfigSyncService flowTaskConfigSyncService;
    @Autowired
    private FlowConfigSyncService flowConfigSyncService;

    /**
     * 用于企业初始化同步任务配置
     */
    @PostMapping(value = "/syncFlowTaskConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    void syncFlowTaskConfig(@RequestBody @Valid SyncCompanyDTO dto) {
        flowTaskConfigSyncService.syncData(dto);
    }

    /**
     * 用于企业初始化同步流程配置
     */
    @PostMapping(value = "/syncFlowConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    void syncFlowConfig(@RequestBody @Valid SyncCompanyDTO dto) {
        flowConfigSyncService.syncData(dto);
    }
}
