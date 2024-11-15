package cn.cuiot.dmp.baseconfig.controller;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.baseconfig.custommenu.service.FlowTaskConfigSyncService;
import cn.cuiot.dmp.baseconfig.flow.service.FlowConfigSyncService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
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
    public IdmResDTO<?> syncFlowTaskConfig(@RequestBody @Valid SyncCompanyDTO dto) {
        flowTaskConfigSyncService.syncData(dto);
        return IdmResDTO.success();
    }

    /**
     * 用于企业初始化同步流程配置
     */
    @PostMapping(value = "/syncFlowConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<?> syncFlowConfig(@RequestBody @Valid SyncCompanyDTO dto) {
        flowConfigSyncService.syncData(dto);
        return IdmResDTO.success();
    }

    /**
     * 用于清空企业初始化同步任务配置
     */
    @PostMapping(value = "/cleanSyncFlowTaskConfigData", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<?> cleanSyncFlowTaskConfigData(@RequestBody @Valid SyncCompanyDTO dto) {
        flowTaskConfigSyncService.cleanSyncData(dto);
        return IdmResDTO.success();
    }

    /**
     * 用于清空企业初始化同步流程配置
     */
    @PostMapping(value = "/cleanSyncFlowConfigData", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<?> cleanSyncFlowConfigData(@RequestBody @Valid SyncCompanyDTO dto) {
        flowConfigSyncService.cleanSyncData(dto);
        return IdmResDTO.success();
    }
}
