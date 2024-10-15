package cn.cuiot.dmp.largescreen.service.feign;//	模板


import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.vo.WorkInfoStatisticVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 合同Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@FeignClient(value = "community-config",fallbackFactory = ConfigFeignServiceFallback.class)
public interface ConfigFeignService {


    /**
     * 工单统计
     * @param dto
     * @return
     */
    @PostMapping("/work/queryWorkOrderStatistic")
    IdmResDTO<WorkInfoStatisticVO> queryWorkOrderStatistic(@RequestBody StatisInfoReqDTO dto);

}
