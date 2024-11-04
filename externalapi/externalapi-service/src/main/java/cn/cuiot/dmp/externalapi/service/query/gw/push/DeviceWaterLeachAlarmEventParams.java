package cn.cuiot.dmp.externalapi.service.query.gw.push;

import lombok.Data;

/**
 * 格物水浸报警器事件推送
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Data
public class DeviceWaterLeachAlarmEventParams {

    /**
     * 故障代码（0无故障，1故障）
     */
    private String errorCode;
}
