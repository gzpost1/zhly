package cn.cuiot.dmp.largescreen.service.feign;

import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.archive.BuildingArchivesPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.ArchivesStatisticVO;
import cn.cuiot.dmp.largescreen.service.vo.BuildingArchivesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ArchiveApiFeignServiceFallback implements FallbackFactory<ArchiveApiFeignService> {
    @Override
    public ArchiveApiFeignService create(Throwable cause) {
        return new ArchiveApiFeignService() {
            @Override
            public IdmResDTO<List<BuildingArchivesVO>> queryArchiveInfoList(BuildingArchivesPageQuery pageQuery) {
                log.error("queryArchiveInfoList",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public IdmResDTO<ArchivesStatisticVO> queryArchiveBaseStatisticInfo(BuildingArchivesPageQuery pageQuery) {
                log.error("queryArchiveBaseStatisticInfo",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
