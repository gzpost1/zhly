package cn.cuiot.dmp.largescreen.service.feign;

import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.ContractLeaseStatisticVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public  class LeaseFeignServiceFallback implements FallbackFactory<LeaseFeignService> {
    @Override
    public LeaseFeignService create(Throwable cause) {
        return new LeaseFeignService() {
            @Override
            public IdmResDTO<ContractLeaseStatisticVO> contractLeaseArchiveStatistic(BuildingArchivesPageQuery idsReq) {
                log.error("contractLeaseArchiveStatistic",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
