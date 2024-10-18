package cn.cuiot.dmp.largescreen.service.feign;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.content.NoticeStatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.vo.ContentNoticeVO;
import cn.cuiot.dmp.largescreen.service.vo.NoticeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContentFeignServiceFallback implements FallbackFactory<ContentFeignService> {
    @Override
    public ContentFeignService create(Throwable cause) {
        return new ContentFeignService() {
            @Override
            public IdmResDTO<Page<ContentNoticeVO>> queryContentNotice(NoticeStatisInfoReqDTO dto) {
                log.error("queryContentNotice",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);

            }

            @Override
            public IdmResDTO<NoticeVo> queryForDetail(IdParam idParam) {
                log.error("queryForDetail",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
