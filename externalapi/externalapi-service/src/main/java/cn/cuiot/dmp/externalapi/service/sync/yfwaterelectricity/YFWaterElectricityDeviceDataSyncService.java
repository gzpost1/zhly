package cn.cuiot.dmp.externalapi.service.sync.yfwaterelectricity;

import cn.cuiot.dmp.externalapi.service.entity.yfwaterelectricity.*;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.*;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.feign.YuFanFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 宇泛 水，电表设备数据同步
 *
 * @author: xiaotao
 * @date: 2024/10/11 9:48
 */
@Slf4j
@Component
public class YFWaterElectricityDeviceDataSyncService {

    @Autowired
    private YuFanFeignService yuFanFeignService;

    @Autowired
    private IYfWaterMeterService yfWaterMeterService;

    @Autowired
    private IYfWaterMeterStatisticsDayService iYfWaterMeterStatisticsDayService;

    @Autowired
    private IYfWaterMeterStatisticsRealService yfWaterMeterStatisticsRealService;

    @Autowired
    private IYfElectricityMeterService yfElectricityMeterService;

    @Autowired
    private IYfElectricityMeterStatisticsDayService yfElectricityMeterStatisticsDayService;

    @Autowired
    private IYfElectricityMeterStatisticsRealService yfElectricityMeterStatisticsRealService;


    public void electricityDeviceDataSyncDay(LocalDateTime startTime, LocalDateTime endTime) {

        for (LocalDateTime i = startTime; i.isBefore(endTime); i = i.plusDays(1)) {
            // 电表设备列表
            List<YfElectricityMeter> list = yfElectricityMeterService.list();
            for (YfElectricityMeter yfElectricityMeter : list) {
                YfElectricityMeterStatisticsDay statisticsDay = new YfElectricityMeterStatisticsDay();
                statisticsDay.setMeterId(yfElectricityMeter.getId());
                statisticsDay.setDeviceNo(yfElectricityMeter.getDeviceNo());
                statisticsDay.setRecordTime(i);
                statisticsDay.setAmount(BigDecimal.valueOf(RandomUtils.nextFloat(10, 1000)).setScale(4, BigDecimal.ROUND_HALF_UP));
                yfElectricityMeterStatisticsDayService.save(statisticsDay);
            }
        }
    }


    public void electricityDeviceDataSyncReal() {
        // 电表设备列表
        List<YfElectricityMeter> list = yfElectricityMeterService.list();
        for (YfElectricityMeter yfElectricityMeter : list) {
            YfElectricityMeterStatisticsReal statisticsReal = new YfElectricityMeterStatisticsReal();
            statisticsReal.setMeterId(yfElectricityMeter.getId());
            statisticsReal.setDeviceNo(yfElectricityMeter.getDeviceNo());
            statisticsReal.setRecordTime(LocalDateTime.now());
            statisticsReal.setAmount(BigDecimal.valueOf(RandomUtils.nextFloat(10, 1000)).setScale(4, BigDecimal.ROUND_HALF_UP));
            yfElectricityMeterStatisticsRealService.save(statisticsReal);
        }

    }


    public void waterDeviceDataSyncDay(LocalDateTime startTime, LocalDateTime endTime) {
        // 水表设备列表
        List<YfWaterMeter> list = yfWaterMeterService.list();
        for (LocalDateTime i = startTime; i.isBefore(endTime); i = i.plusDays(1)) {
            for (YfWaterMeter yfWaterMeter : list) {
                YfWaterMeterStatisticsDay statisticsDay = new YfWaterMeterStatisticsDay();
                statisticsDay.setMeterId(yfWaterMeter.getId());
                statisticsDay.setDeviceNo(yfWaterMeter.getDeviceNo());
                statisticsDay.setRecordTime(i);
                statisticsDay.setAmount(BigDecimal.valueOf(RandomUtils.nextFloat(10, 1000)).setScale(4, BigDecimal.ROUND_HALF_UP));
                iYfWaterMeterStatisticsDayService.save(statisticsDay);
            }
        }
    }


    public void waterDeviceDataSyncReal() {
        // 电表设备列表
        List<YfWaterMeter> list = yfWaterMeterService.list();
        for (YfWaterMeter yfWaterMeter : list) {
            YfWaterMeterStatisticsReal statisticsReal = new YfWaterMeterStatisticsReal();
            statisticsReal.setMeterId(yfWaterMeter.getId());
            statisticsReal.setDeviceNo(yfWaterMeter.getDeviceNo());
            statisticsReal.setRecordTime(LocalDateTime.now());
            statisticsReal.setAmount(BigDecimal.valueOf(RandomUtils.nextFloat(10, 1000)).setScale(4, BigDecimal.ROUND_HALF_UP));
            yfWaterMeterStatisticsRealService.save(statisticsReal);
        }
    }

}
