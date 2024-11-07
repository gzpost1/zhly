package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceCreateResp implements Serializable {

    private static final long serialVersionUID = 6825047195875556522L;

    /**
     * 设备所属的产品key。
     */
    private String productKey;

    /**
     * 设备标识符，新建设备时填写。(租户下唯一)
     */
    private String deviceKey;

    /**
     * 设备名称。（新建设备时填写）
     */
    private String deviceName;

    /**
     * iotId
     */
    private String iotId;

    /**
     * 设备IMEI。
     */
    private String imei;

    /**
     * 设备密钥，平台自动生成，全平台唯一设备鉴权时使用。(MQTT、HTTP）
     */
    private String deviceSecret;

    /**
     * 备注信息。
     */
    private String description;
}
