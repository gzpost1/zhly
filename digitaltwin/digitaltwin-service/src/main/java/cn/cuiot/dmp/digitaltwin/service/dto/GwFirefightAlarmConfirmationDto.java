package cn.cuiot.dmp.digitaltwin.service.dto;

import lombok.Data;

/**
 * 格物消防-报警确认dto
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
public class GwFirefightAlarmConfirmationDto {
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
     * 上报时间（yyyy-MM-dd HH:mm:ss）
     */
    private String reportTime;

    /**
     * 火警/故障处 理状态
     */
    private String alarmStatus;

    /**
     * 火警处理结果
     */
    private String dealType;

    /**
     * 故障处理结果
     */
    private String disposeResult;

    /**
     * 处理描述
     */
    private String alarmDetail;

    /**
     * 区县编码
     */
    private String areaCode;
}
