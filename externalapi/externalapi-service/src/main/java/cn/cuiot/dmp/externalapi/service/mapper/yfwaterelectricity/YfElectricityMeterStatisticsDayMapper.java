package cn.cuiot.dmp.externalapi.service.mapper.yfwaterelectricity;

import cn.cuiot.dmp.base.application.mybatis.mapper.BaseMybatisMapper;
import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.YfElectricityMeterStatisticsDay;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 智慧物联-宇泛电表-同步天水表用量数据 Mapper 接口
 * </p>
 *
 * @author xiaotao
 * @since 2024-10-11
 */
public interface YfElectricityMeterStatisticsDayMapper extends BaseMybatisMapper<YfElectricityMeterStatisticsDay> {


    List<YfElectricityMeterStatisticsDay> queryDayAmount(@Param("params") YfElectricityMeterDTO params);
}
