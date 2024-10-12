package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsReal;

import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛电表-同步实时水表用量数据 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
public interface IYfElectricityMeterStatisticsRealService extends BaseService<YfElectricityMeterStatisticsReal> {

    /**
     * 根据电表ID查询实时水表用量数据
     * @param meterIds
     * @return
     */
    List<YfElectricityMeterStatisticsReal> queryRealAmount(List<Long> meterIds);
}
