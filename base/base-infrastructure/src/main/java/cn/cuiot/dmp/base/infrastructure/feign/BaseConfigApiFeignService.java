package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: zc
 * @Date: 2024-11-11
 */
@Component
@FeignClient(value = "community-config")
public interface BaseConfigApiFeignService {

    /**
     * 用于企业初始化同步任务配置
     */
    @PostMapping(value = "/api/syncFlowTaskConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<Boolean> syncFlowTaskConfig(@RequestBody SyncCompanyDTO dto);

    /**
     * 用于企业初始化同步流程配置
     */
    @PostMapping(value = "/api/syncFlowConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<Boolean> syncFlowConfig(@RequestBody SyncCompanyDTO dto);
}
