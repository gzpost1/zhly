package cn.cuiot.dmp.digitaltwin.provider.consumer;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.digitaltwin.provider.consumer.config.DigitaltwinMsgBaseChannel;
import cn.cuiot.dmp.digitaltwin.service.dto.*;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightAlarmConfirmationService;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightDeviceMonitorService;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightDeviceService;
import cn.cuiot.dmp.digitaltwin.service.service.GwFirefightRealTimeAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 格物消防-推送数据消费
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Slf4j
@Component
public class GwFireFightReceiver {
    @Autowired
    private GwFirefightDeviceService deviceService;
    @Autowired
    private GwFirefightRealTimeAlarmService realTimeAlarmService;
    @Autowired
    private GwFirefightAlarmConfirmationService alarmConfirmationService;
    @Autowired
    private GwFirefightDeviceMonitorService deviceMonitorService;

    /**
     * 设备信息
     *
     * @param dto 参数
     */
    @StreamListener(DigitaltwinMsgBaseChannel.GWFIREFIGHTDEVICEINPUT)
    public void receiveDeviceInput(@Payload GwFirefightDeviceDto dto) {
        log.info("格物消防【设备信息】入库操作" + JsonUtil.writeValueAsString(dto));
        if (Objects.nonNull(dto)) {
            deviceService.save(dto);
        }
    }

    /**
     * 设备状态
     *
     * @param dto 参数
     */
    @StreamListener(DigitaltwinMsgBaseChannel.GWFIREFIGHTDEVICESTATUSINPUT)
    public void receiveDeviceStatusInput(@Payload GwFirefightDeviceStatusDto dto) {
        log.info("格物消防【设备状态】入库操作" + JsonUtil.writeValueAsString(dto));
        if (Objects.nonNull(dto)) {
            deviceService.updateStatus(dto);
        }
    }

    /**
     * 实时报警
     *
     * @param dto 参数
     */
    @StreamListener(DigitaltwinMsgBaseChannel.GWFIREFIGHTREALTIMEALARMINPUT)
    public void receiveRealTimeAlarmInput(@Payload GwFirefightRealTimeAlarmDto dto) {
        log.info("格物消防【实时报警】入库操作" + JsonUtil.writeValueAsString(dto));
        if (Objects.nonNull(dto)) {
            realTimeAlarmService.save(dto);
        }
    }

    /**
     * 报警确认
     *
     * @param dto 参数
     */
    @StreamListener(DigitaltwinMsgBaseChannel.GWFIREFIGHTALARMCONFIRMATIONINPUT)
    public void receiveAlarmConfirmationInput(@Payload GwFirefightAlarmConfirmationDto dto) {
        log.info("格物消防【报警确认】入库操作" + JsonUtil.writeValueAsString(dto));
        if (Objects.nonNull(dto)) {
            alarmConfirmationService.save(dto);
        }
    }

    /**
     * 设备监控
     *
     * @param dto 参数
     */
    @StreamListener(DigitaltwinMsgBaseChannel.GWFIREFIGHTDEVICEMONITORINPUT)
    public void receiveDeviceMonitorInput(@Payload GwFirefightDeviceMonitorDto dto) {
        log.info("格物消防【设备监控】入库操作" + JsonUtil.writeValueAsString(dto));
        if (Objects.nonNull(dto)) {
            deviceMonitorService.save(dto);
        }
    }
}
