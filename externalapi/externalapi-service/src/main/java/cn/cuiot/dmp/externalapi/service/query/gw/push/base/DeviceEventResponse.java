package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备事件响应
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Data
@NoArgsConstructor
public class DeviceEventResponse<T> {
    private List<DataItem<T>> deviceEventOutParams;
    private String deviceEventType;
    private String deviceEventName;
    private String deviceEventKey;
    private String productKey;
    private String deviceKey;
}
