package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;

import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛电表-同步天电表用量数据 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
public interface IYfElectricityMeterStatisticsDayService extends BaseService<YfElectricityMeterStatisticsDay> {

    /**
     * 获取时间段内电表用量数据
     * @param params YfElectricityMeterDTO
     * @return
     */
    List<YfElectricityMeterStatisticsDay> queryDayAmount(YfElectricityMeterDTO params);
}
