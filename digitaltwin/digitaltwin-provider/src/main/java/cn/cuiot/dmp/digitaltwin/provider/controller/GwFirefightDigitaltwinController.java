package cn.cuiot.dmp.digitaltwin.provider.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.digitaltwin.base.auth.ThirdRequestNeedAuth;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightAlarmConfirmationService;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightDeviceService;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightRealTimeAlarmService;
import cn.cuiot.dmp.digitaltwin.service.entity.vo.GwFirefightAlarmConfirmationVo;
import cn.cuiot.dmp.digitaltwin.service.entity.vo.GwFirefightRealTimeAlarmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消防格物-提供给数字孪生api接口
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Slf4j
@RestController
@RequestMapping("/gwFirefight/digitaltwin")
public class GwFirefightDigitaltwinController {

    @Autowired
    private GwFirefightDeviceService deviceService;
    @Autowired
    private GwFirefightRealTimeAlarmService realTimeAlarmService;
    @Autowired
    private GwFirefightAlarmConfirmationService alarmConfirmationService;

    /**
     * 烟感监测-设备状态
     *
     * @return IdmResDTO
     * @Param
     */
    @PostMapping("/queryDeviceStatus")
    @ThirdRequestNeedAuth
    public IdmResDTO<String> queryDeviceStatus(@RequestBody GwFirefightDeviceQuery query) {
        return IdmResDTO.success(deviceService.queryDeviceStatus(query));
    }

    /**
     * 烟感监测-报警消息（近7天数据）
     *
     * @return IdmResDTO
     * @Param
     */
    @PostMapping("/queryAlarm")
    @ThirdRequestNeedAuth
    public IdmResDTO<List<GwFirefightRealTimeAlarmVo>> queryAlarm(@RequestBody GwFirefightDeviceQuery query) {
        return IdmResDTO.success(realTimeAlarmService.queryAlarm(query));
    }

    /**
     * 烟感监测-报警处理消息（近7天数据）
     *
     * @return IdmResDTO
     * @Param
     */
    @PostMapping("/queryAlarmConfirmation")
    @ThirdRequestNeedAuth
    public IdmResDTO<List<GwFirefightAlarmConfirmationVo>> queryAlarmConfirmation(@RequestBody GwFirefightDeviceQuery query) {
        return IdmResDTO.success(alarmConfirmationService.queryAlarmConfirmation(query));
    }
}
