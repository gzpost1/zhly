package cn.cuiot.dmp.largescreen.service.feign;

import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.dto.external.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.external.VideoPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.ContractLeaseStatisticVO;
import cn.cuiot.dmp.largescreen.service.vo.EntranceGuardRecordVo;
import cn.cuiot.dmp.largescreen.service.vo.IOTStatisticVo;
import cn.cuiot.dmp.largescreen.service.vo.VideoPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public  class ExternalApiFeignServiceFallback implements FallbackFactory<ExternalApiFeignService> {
    @Override
    public ExternalApiFeignService create(Throwable cause) {
        return new ExternalApiFeignService() {

            @Override
            public IdmResDTO<Page<VideoPageVo>> queryForPage(VideoPageQuery query) {
                log.error("queryForPage",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public IdmResDTO<IOTStatisticVo> queryIotStatistic(StatisInfoReqDTO reqDTO) {
                log.error("queryIotStatistic",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public IdmResDTO<Page<EntranceGuardRecordVo>> entranceGuardQueryForPage(EntranceGuardRecordReqDTO query) {
                log.error("entranceGuardQueryForPage",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
