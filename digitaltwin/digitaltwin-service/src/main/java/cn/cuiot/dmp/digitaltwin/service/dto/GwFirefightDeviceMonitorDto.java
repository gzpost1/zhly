package cn.cuiot.dmp.digitaltwin.service.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 格物消防-设备监测dto
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
public class GwFirefightDeviceMonitorDto {
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
     * 设备检测数据，json字符串
     */
    private List<Map<String, Object>> outputParams;

    /**
     * 区县编码
     */
    private String areaCode;
}
