package cn.cuiot.dmp.base.infrastructure.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: zc
 * @Date: 2024-11-11
 */
@Component
@FeignClient(value = "community-lease")
public interface LeaseApiFeignService {

    /**
     * 企业初始化-同步收费标准
     */
    @PostMapping(value = "/api/syncChargeStandard", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<?> syncChargeStandard(@RequestBody @Valid SyncCompanyDTO dto);
}
