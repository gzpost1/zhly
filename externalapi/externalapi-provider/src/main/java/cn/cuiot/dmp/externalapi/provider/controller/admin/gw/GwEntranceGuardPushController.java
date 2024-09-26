package cn.cuiot.dmp.externalapi.provider.controller.admin.gw;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceEventResponse;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceResponse;
import cn.cuiot.dmp.externalapi.service.service.gw.push.GwPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * DMP 平台数据推送
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Slf4j
@RestController
@RequestMapping("/gw/dmpDataPush")
public class GwEntranceGuardPushController {

    @Autowired
    private GwPushService gwPushService;

    /**
     * 服务调用 数据推送
     */
    @PostMapping("/invokeDeviceService")
    public IdmResDTO<?> invokeDeviceService(@RequestBody DeviceServiceResponse<Object> resp) {
        log.info("接收dmp平台转发【设备调用】数据..........resp:{}", JsonUtil.writeValueAsString(resp));
        gwPushService.invokeDeviceService(resp);
        return IdmResDTO.success();
    }

    /**
     * 事件 数据推送
     */
    @PostMapping("/deviceEvent")
    public IdmResDTO<?> deviceEvent(@RequestBody DeviceEventResponse<Object> resp) {
        log.info("接收dmp平台转发【事件】数据..........resp:{}", JsonUtil.writeValueAsString(resp));
        gwPushService.deviceEvent(resp);
        return IdmResDTO.success();
    }
}
