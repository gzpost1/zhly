package cn.cuiot.dmp.digitaltwin.provider.controller;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.digitaltwin.base.auth.ThirdPushNeedAuth;
import cn.cuiot.dmp.digitaltwin.base.bean.GwFirefightJsonResult;
import cn.cuiot.dmp.digitaltwin.provider.rocketmq.DigitaltwinSendService;
import cn.cuiot.dmp.digitaltwin.service.entity.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 格物消防数据-第三方推送
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Slf4j
@RestController
@RequestMapping("/gwFirefight/push")
public class GwFirefightPushController {

    @Autowired
    private DigitaltwinSendService digitaltwinSendService;

    /**
     * 设备信息
     */
    @PostMapping("/deviceInfo")
    @ThirdPushNeedAuth
    public GwFirefightJsonResult deviceInfo(@RequestBody GwFirefightDeviceDto dto) {
        log.info("接收格物消防【设备信息】推送数据.............." + JsonUtil.writeValueAsString(dto));
        try {
            digitaltwinSendService.sendDeviceInfo(dto);
            return GwFirefightJsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return GwFirefightJsonResult.error();
        }
    }

    /**
     * 设备状态变更
     */
    @PostMapping("/deviceStatus")
    @ThirdPushNeedAuth
    public GwFirefightJsonResult deviceStatus(@RequestBody GwFirefightDeviceStatusDto dto) {
        log.info("接收格物消防【设备状态变更】推送数据.............." + JsonUtil.writeValueAsString(dto));
        try {
            digitaltwinSendService.deviceStatus(dto);
            return GwFirefightJsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return GwFirefightJsonResult.error();
        }
    }

    /**
     * 实时报警
     */
    @PostMapping("/realTimeAlarm")
    @ThirdPushNeedAuth
    public GwFirefightJsonResult realTimeAlarm(@RequestBody GwFirefightRealTimeAlarmDto dto) {
        log.info("接收格物消防【实时报警】推送数据.............." + JsonUtil.writeValueAsString(dto));
        try {
            digitaltwinSendService.realTimeAlarm(dto);
            return GwFirefightJsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return GwFirefightJsonResult.error();
        }
    }

    /**
     * 报警确认
     */
    @PostMapping("/alarmConfirmation")
    @ThirdPushNeedAuth
    public GwFirefightJsonResult alarmConfirmation(@RequestBody GwFirefightAlarmConfirmationDto dto) {
        log.info("接收格物消防【报警确认】推送数据.............." + JsonUtil.writeValueAsString(dto));
        try {
            digitaltwinSendService.alarmConfirmation(dto);
            return GwFirefightJsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return GwFirefightJsonResult.error();
        }
    }

    /**
     * 设备监控
     */
    @PostMapping("/deviceMonitor")
    @ThirdPushNeedAuth
    public GwFirefightJsonResult deviceMonitor(@RequestBody GwFirefightDeviceMonitorDto dto) {
        log.info("接收格物消防【设备监控】推送数据.............." + JsonUtil.writeValueAsString(dto));
        try {
            digitaltwinSendService.deviceMonitor(dto);
            return GwFirefightJsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return GwFirefightJsonResult.error();
        }
    }
}
