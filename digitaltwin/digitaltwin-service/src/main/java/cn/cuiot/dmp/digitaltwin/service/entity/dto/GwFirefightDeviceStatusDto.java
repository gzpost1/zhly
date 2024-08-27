package cn.cuiot.dmp.digitaltwin.service.entity.dto;

import lombok.Data;

/**
 * 格物消防-设备状态dto
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
public class GwFirefightDeviceStatusDto {
    /**
     * 上下线状态
     */
    private String status;

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
     * 上报时间
     */
    private String reportTime;

    /**
     * 区县编码
     */
    private String areaCode;
}
