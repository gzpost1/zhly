package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.feign;

import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.EquipmentCommandControllerReq;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.EquipmentControllerReq;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.EquipmentEventCommandControllerReq;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.MeterCommandControlResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YuFanFeignServiceFallback implements FallbackFactory<YuFanFeignService> {
    @Override
    public YuFanFeignService create(Throwable cause) {
        return new YuFanFeignService() {

            @Override
            public MeterCommandControlResp<?> addMeter(EquipmentControllerReq req) {
                log.error("addMeter",cause);
                return MeterCommandControlResp.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public MeterCommandControlResp<?> updateMeter(EquipmentControllerReq req) {
                log.error("updateMeter",cause);
                return MeterCommandControlResp.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public MeterCommandControlResp<?> deleteMeter(EquipmentCommandControllerReq req) {
                log.error("deleteMeter",cause);
                return MeterCommandControlResp.error(ErrorCode.BUSINESS_EXCEPTION);
            }

            @Override
            public MeterCommandControlResp<?> eventMeter(EquipmentEventCommandControllerReq req) {
                log.error("eventMeter",cause);
                return MeterCommandControlResp.error(ErrorCode.BUSINESS_EXCEPTION);
            }
        };
    }
}
