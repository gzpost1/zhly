package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfWaterMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;

import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛水表-同步天水表用量数据 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
public interface IYfWaterMeterStatisticsDayService extends BaseService<YfWaterMeterStatisticsDay> {

    /**
     * 按天查询天水表用量数据
     * @param vo YfWaterMeterDTO
     * @return YfWaterMeterStatisticsDay
     */
    List<YfWaterMeterStatisticsDay> queryDayAmount(YfWaterMeterDTO vo);
}
