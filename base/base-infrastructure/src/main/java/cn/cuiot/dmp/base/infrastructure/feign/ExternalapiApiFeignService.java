package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.*;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 系统管理Feign服务
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:46
 */
@Component
@FeignClient(value = "community-externalapi")
public interface ExternalapiApiFeignService {

    /**
     * 根据企业id查询企业名称列表
     */
    @PostMapping(value = "/api/queryPlatfromSmsRedis", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<PlatfromInfoRespDTO> queryPlatfromSmsRedis(@RequestBody PlatfromInfoReqDTO dto);
}
