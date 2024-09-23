package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import lombok.Data;

/**
 * @Author: zc
 * @Date: 2024-09-13
 */
@Data
public class DeviceServiceResponse<T> {
    private DeviceServiceOutParams<T> deviceServiceOutParams;
    private String deviceServiceKey;
    private String deviceServiceName;
    private String deviceKey;
    private String productKey;
}
