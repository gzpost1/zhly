package cn.cuiot.dmp.largescreen.service.feign;//	模板

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.external.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.largescreen.service.dto.external.VideoPageQuery;
import cn.cuiot.dmp.largescreen.service.vo.EntranceGuardRecordVo;
import cn.cuiot.dmp.largescreen.service.vo.IOTStatisticVo;
import cn.cuiot.dmp.largescreen.service.vo.VideoPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * 合同Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@FeignClient(value = "community-externalapi",fallbackFactory = ExternalApiFeignServiceFallback.class)
public interface ExternalApiFeignService {


    /**
     * 查看视频分页数据
     * @param query VideoPageQuery
     * @return
     */
    @RequestMapping("/api/video/queryForPage")
    IdmResDTO<IPage<VideoPageVo>> queryForPage(@RequestBody VideoPageQuery query);


    @RequestMapping("/api/queryIotStatistic")
     IdmResDTO<IOTStatisticVo> queryIotStatistic(@RequestBody StatisInfoReqDTO reqDTO);

    @PostMapping("/api/entranceGuard/queryForPage")
    IdmResDTO<IPage<EntranceGuardRecordVo>> entranceGuardQueryForPage(@RequestBody EntranceGuardRecordReqDTO query);


}
