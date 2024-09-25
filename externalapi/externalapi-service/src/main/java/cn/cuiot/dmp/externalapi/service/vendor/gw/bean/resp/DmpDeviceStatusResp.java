package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceStatusResp implements Serializable {

    private static final long serialVersionUID = -5204705336322841635L;

    /**
     * 设备标识符，新建设备时填写。(租户下唯一)
     */
    private String deviceKey;

    /**
     * 设备名称。（新建设备时填写）
     */
    private String deviceName;

    /**
     * 落地的产品id
     */
    private Long productId;

    /**
     * 设备状态。(0: 在线，1: 离线，2: 未激活）
     */
    private String status;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte enabled;

}
