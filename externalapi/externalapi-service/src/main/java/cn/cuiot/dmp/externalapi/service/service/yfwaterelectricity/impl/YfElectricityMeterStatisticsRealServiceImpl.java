package cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.impl;


import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsReal;
import cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity.YfElectricityMeterStatisticsRealMapper;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterStatisticsRealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛电表-同步实时水表用量数据 服务实现类
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
@Service
public class YfElectricityMeterStatisticsRealServiceImpl extends BaseMybatisServiceImpl<YfElectricityMeterStatisticsRealMapper, YfElectricityMeterStatisticsReal> implements IYfElectricityMeterStatisticsRealService {

    @Override
    public List<YfElectricityMeterStatisticsReal> queryRealAmount(List<Long> meterIds) {
        LambdaQueryWrapper<YfElectricityMeterStatisticsReal> wrapper = Wrappers.<YfElectricityMeterStatisticsReal>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(meterIds),YfElectricityMeterStatisticsReal::getMeterId, meterIds);
        return getBaseMapper().selectList(wrapper);
    }
}
