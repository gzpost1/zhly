package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

/**
 * 设备-调用设备服务resp
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Data
public class InvokeDeviceServiceResp {

    /**
     * 平台下发给设备设置属性的消息ID
     */
    private String messageId;

    /**
     * 如果是同步调用服务，返回的调用结果。如果是异步调用服务，不返回此参数。
     */
    private String result;
}
