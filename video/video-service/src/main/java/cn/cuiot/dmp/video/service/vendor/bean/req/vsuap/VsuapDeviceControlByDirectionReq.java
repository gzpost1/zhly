
package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-云台水平垂直方向控制req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapDeviceControlByDirectionReq {
    /**
     * 通道编码ID（必填）
     */
    private String channelCodeId;
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 云台水平方向控制。0：停止；1：向左；2：向右（必填）
     */
    private String horizontal;
    /**
     * 云台水平方向相对速度，取值0~255, 值越大速度越快（必填）
     */
    private String horizontalSpeed;
    /**
     * 云台垂直方向控制。0：停止；1：向上；2：向下（必填）
     */
    private String vertical;
    /**
     * 云台垂直方向相对速度，取值0~255, 值越大速度越快
     */
    private String verticalSpeed;

}
