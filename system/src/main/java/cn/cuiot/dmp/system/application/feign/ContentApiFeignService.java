package cn.cuiot.dmp.system.application.feign;//	模板

import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/19 14:12
 */
@Component
@FeignClient(value = "community-content")
public interface ContentApiFeignService {

    @PostMapping(value = "/api/initModule", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<Boolean> initModule(@RequestParam("orgId") Long orgId);
}
