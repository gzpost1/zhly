package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfElectricityMeterStatisticsDayMapper;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterStatisticsDayService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛电表-同步天水表用量数据 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Service
public class YfElectricityMeterStatisticsDayServiceImpl extends BaseMybatisServiceImpl<YfElectricityMeterStatisticsDayMapper, YfElectricityMeterStatisticsDay> implements IYfElectricityMeterStatisticsDayService {

    @Override
    public List<YfElectricityMeterStatisticsDay> queryDayAmount(YfElectricityMeterDTO params) {
        return getBaseMapper().queryDayAmount(params);
    }
}
