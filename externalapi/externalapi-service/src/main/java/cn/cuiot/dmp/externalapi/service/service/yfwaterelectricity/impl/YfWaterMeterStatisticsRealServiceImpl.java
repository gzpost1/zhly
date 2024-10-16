package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsReal;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfWaterMeterStatisticsReal;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfWaterMeterStatisticsRealMapper;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfWaterMeterStatisticsRealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛水表-同步实时水表用量数据 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Service
public class YfWaterMeterStatisticsRealServiceImpl extends BaseMybatisServiceImpl<YfWaterMeterStatisticsRealMapper, YfWaterMeterStatisticsReal> implements IYfWaterMeterStatisticsRealService {

    @Override
    public List<YfWaterMeterStatisticsReal> queryRealAmount(List<Long> meterIds) {
        LambdaQueryWrapper<YfWaterMeterStatisticsReal> wrapper = Wrappers.<YfWaterMeterStatisticsReal>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(meterIds),YfWaterMeterStatisticsReal::getMeterId, meterIds);
        return getBaseMapper().selectList(wrapper);
    }
}
