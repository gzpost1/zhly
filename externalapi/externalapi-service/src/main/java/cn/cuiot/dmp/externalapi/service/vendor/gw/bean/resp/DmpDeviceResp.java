package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceResp implements Serializable {

    private static final long serialVersionUID = 6825047195875556522L;

    /**
     * 设备标识符，新建设备时填写。(租户下唯一)
     */
    private String deviceKey;

    /**
     * 设备名称。（新建设备时填写）
     */
    private String deviceName;

    /**
     * 设备所属的产品key。
     */
    private String productKey;

    /**
     * 产品名称。
     */
    private String productName;

    /**
     * iotId
     */
    private String iotId;

    /**
     * ip
     */
    private String ip;

    /**
     * 节点类型。(0: 设备，1: 网关)
     */
    private String nodeType;

    /**
     * 设备接入协议。(0: MQTT，1: CoAP/LWM2M, 2: HTTP,3:TCP)
     */
    private String deviceConnectionProtocol;

    /**
     * 认证方式。(0: 设备密钥，1: X.509证书)
     */
    private String authType;

    /**
     * 设备的标签列表。
     */
    private List<String> deviceTagList;

    /**
     * 设备的位置信息。
     */
    private String position;

    /**
     * 备注信息。
     */
    private String description;

    /**
     * 设备IMEI。
     */
    private String imei;

    /**
     * 设备状态。(0: 在线，1: 离线，2: 未激活）
     */
    private String status;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte enabled;

    // 根据设备接入协议分为三种类型的设备，包含不同的属性值
    // 1. MQTT、HTTP
    /**
     * 设备密钥，平台自动生成，全平台唯一设备鉴权时使用。(MQTT、HTTP）
     */
    private String deviceSecret;

    // 2. Lwm2m
    /**
     * 订阅模式：0-自动订阅；1-手动订阅。
     */
    private String observeMode;

    // 3. TCP
    /**
     * 设备鉴权码
     */
    private String authCode;


}
