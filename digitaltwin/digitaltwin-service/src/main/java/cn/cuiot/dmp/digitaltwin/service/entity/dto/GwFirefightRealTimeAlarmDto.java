package cn.cuiot.dmp.digitaltwin.service.entity.dto;

import lombok.Data;

/**
 * 格物消防-实时报警dto
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
public class GwFirefightRealTimeAlarmDto {
    /**
     * 消息编号
     */
    private String messageId;

    /**
     * 设备 id
     */
    private String deviceId;

    /**
     * 设备编码
     */
    private String imei;

    /**
     * 事件类别
     */
    private String type;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 上报时间（yyyy-MM-dd HH:mm:ss）
     */
    private String reportTime;

    /**
     * 火警/故障处 理状态
     */
    private String alarmStatus;

    /**
     * 区县编码
     */
    private String areaCode;
}
