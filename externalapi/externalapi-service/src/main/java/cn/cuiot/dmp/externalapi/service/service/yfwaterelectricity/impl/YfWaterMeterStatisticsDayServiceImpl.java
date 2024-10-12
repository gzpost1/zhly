package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfWaterMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfWaterMeterStatisticsDayMapper;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfWaterMeterStatisticsDayService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛水表-同步天水表用量数据 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Service
public class YfWaterMeterStatisticsDayServiceImpl extends BaseMybatisServiceImpl<YfWaterMeterStatisticsDayMapper, YfWaterMeterStatisticsDay> implements IYfWaterMeterStatisticsDayService {

    @Override
    public List<YfWaterMeterStatisticsDay> queryDayAmount(YfWaterMeterDTO params) {
        return getBaseMapper().queryDayAmount(params);
    }
}
