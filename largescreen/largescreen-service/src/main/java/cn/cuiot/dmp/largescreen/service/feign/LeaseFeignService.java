package cn.cuiot.dmp.largescreen.service.feign;//	模板

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.ContractLeaseStatisticVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 合同Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@FeignClient(value = "community-lease",fallbackFactory = LeaseFeignServiceFallback.class)
public interface LeaseFeignService {

    /**
     * 查询租赁合同按楼盘统计信息
     */
    @PostMapping(value = "/api/contractLeaseArchiveStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<ContractLeaseStatisticVO> contractLeaseArchiveStatistic(@RequestBody @Valid BuildingArchivesPageQuery idsReq);


}
