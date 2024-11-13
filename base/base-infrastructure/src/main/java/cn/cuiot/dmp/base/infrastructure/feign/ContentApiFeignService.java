package cn.cuiot.dmp.base.infrastructure.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/19 14:12
 */
@Component
//@FeignClient(value = "community-content")
@FeignClient(value = "community-content", url = "http://127.0.0.1:8090")
public interface ContentApiFeignService {

    /**
     * 企业初始化-同步小程序配置
     */
    @PostMapping(value = "/api/syncData", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<?> syncData(@RequestBody SyncCompanyDTO dto);
}
