package cn.cuiot.dmp.digitaltwin.provider.rocketmq;

import cn.cuiot.dmp.digitaltwin.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * mq发送
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class DigitaltwinSendService {
    @Autowired
    private DigitaltwinMsgChannel msgChannel;

    /**
     * 发送格物消防-设备信息
     *
     * @param dto 参数
     */
    public void sendDeviceInfo(GwFirefightDeviceDto dto) {
        msgChannel.gwFirefightDeviceOutput().send(messageBuilderObject(dto));
    }

    /**
     * 发送格物消防-设备状态变更
     *
     * @param dto 参数
     */
    public void deviceStatus(GwFirefightDeviceStatusDto dto) {
        msgChannel.gwFirefightDeviceStatusOutput().send(messageBuilderObject(dto));
    }

    /**
     * 实时报警
     *
     * @param dto 参数
     */
    public void realTimeAlarm(GwFirefightRealTimeAlarmDto dto) {
        msgChannel.gwFirefightRealTimeAlarmOutput().send(messageBuilderObject(dto));
    }

    /**
     * 报警确认
     *
     * @param dto 参数
     */
    public void alarmConfirmation(GwFirefightAlarmConfirmationDto dto) {
        msgChannel.gwFirefightAlarmConfirmationOutput().send(messageBuilderObject(dto));
    }

    /**
     * 设备监控
     *
     * @param dto 参数
     */
    public void deviceMonitor(GwFirefightDeviceMonitorDto dto) {
        msgChannel.gwFirefightDeviceMonitorOutput().send(messageBuilderObject(dto));
    }

    public <T> Message<T> messageBuilderObject(T data) {
        return MessageBuilder.withPayload(data)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }

}
