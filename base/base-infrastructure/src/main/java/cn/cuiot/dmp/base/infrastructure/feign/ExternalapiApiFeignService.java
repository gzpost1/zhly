package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.*;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 对接外部系统Feign服务
 *
 * @Author: zc
 * @Date: 2024-09-29
 */
@Component
@FeignClient(value = "community-externalapi")
public interface ExternalapiApiFeignService {

    /**
     * 获取平台信息分页
     *
     * @return Page
     * @Param 企业id
     */
    @PostMapping(value = "/api/queryForPage", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<Page<PlatfromInfoRespDTO>> queryForPage(@RequestBody PlatfromInfoReqDTO dto);

    /**
     * 获取平台信息分页
     *
     * @return Page
     * @Param 企业id
     */
    @PostMapping(value = "/api/queryForList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<PlatfromInfoRespDTO>> queryForList(@RequestBody PlatfromInfoReqDTO dto);
}
