package cn.cuiot.dmp.largescreen.service.feign;//	模板



import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import cn.cuiot.dmp.largescreen.service.vo.WorkInfoStatisticVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;



/**
 * 合同Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@Slf4j
public class ConfigFeignServiceFallback implements FallbackFactory<ConfigFeignService> {


    @Override
    public ConfigFeignService create(Throwable cause) {
        return new ConfigFeignService() {
            @Override
            public IdmResDTO<WorkInfoStatisticVO> queryWorkOrderStatistic(StatisInfoReqDTO dto) {
                log.error("queryWorkOrderStatistic",cause);
                return IdmResDTO.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
