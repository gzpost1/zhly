package cn.cuiot.dmp.largescreen.service.feign;//	模板


import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.content.NoticeStatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.vo.ContentNoticeVO;
import cn.cuiot.dmp.largescreen.service.vo.NoticeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Content
 *  Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@FeignClient(value = "community-content",fallbackFactory = ContentFeignServiceFallback.class)
public interface ContentFeignService {


    /**
     * 查看公告统计
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/queryContentNotice")
    IdmResDTO<Page<ContentNoticeVO>> queryContentNotice(@RequestBody NoticeStatisInfoReqDTO dto);


    /**
     * 查看公告详情
     * @param idParam IdParam
     * @return NoticeVo
     */
    @PostMapping("/api/queryForDetail")
    IdmResDTO<NoticeVo> queryForDetail(@RequestBody @Valid IdParam idParam);

}
