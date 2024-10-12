package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfWaterMeterStatisticsReal;

import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛水表-同步实时水表用量数据 服务类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
public interface IYfWaterMeterStatisticsRealService extends BaseService<YfWaterMeterStatisticsReal> {

    /**
     * 查询水表实时用量
     * @param meterIds
     * @return
     */
    List<YfWaterMeterStatisticsReal> queryRealAmount(List<Long> meterIds);
}
