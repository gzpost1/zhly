package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.feign;//	模板

import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.bean.*;
import cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.config.YuFanHeadInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 宇泛Feign服务
 * @author xiaotao
 * @Description
 * @data 2024/9/29 15:11
 */
@Component
@FeignClient(value = "external-yf",url = "${externalapi.yf.baseurl}",configuration = YuFanHeadInterceptor.class,fallbackFactory = YuFanFeignServiceFallback.class)
public interface YuFanFeignService {

    /**
     * 新增设备
     * @param req 设备
     * @return Object
     */
    @PostMapping(value = "/v2/device/create",produces = MediaType.APPLICATION_JSON_VALUE)
    MeterCommandControlResp<?> addMeter(@RequestBody @Valid EquipmentControllerReq req);


    /**
     * 修改设备
     * @param req 设备
     * @return Object
     */
    @PostMapping(value = "/v2/device/update",produces = MediaType.APPLICATION_JSON_VALUE)
    MeterCommandControlResp<?> updateMeter(@RequestBody @Valid EquipmentControllerReq req);


    /**
     * 删除设备
     * @param req 设备
     * @return Object
     */
    @PostMapping(value = "/v2/device/delete",produces = MediaType.APPLICATION_JSON_VALUE)
    MeterCommandControlResp<?> deleteMeter(@RequestBody @Valid EquipmentCommandControllerReq req);


    /**
     * 设备事件交互接口
     * @param req 设备
     * @return Object
     */
    @PostMapping(value = "/v2/interactive/event",produces = MediaType.APPLICATION_JSON_VALUE)
    MeterCommandControlResp<?> eventMeter(@RequestBody @Valid EquipmentEventCommandControllerReq req);

}
