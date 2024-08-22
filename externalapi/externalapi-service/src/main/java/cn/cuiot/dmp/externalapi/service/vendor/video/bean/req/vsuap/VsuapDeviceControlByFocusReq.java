
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-镜头变焦req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapDeviceControlByFocusReq {
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 通道编码ID（必填）
     */
    private String channelCodeId;
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 镜头聚焦控制。0：停止；1：拉近；2：拉远（必填）
     */
    private String focus;
    /**
     * 镜头聚焦控制速度。取值0~255, 值越大速度越快（必填）
     */
    private String focusSpeed;
}
