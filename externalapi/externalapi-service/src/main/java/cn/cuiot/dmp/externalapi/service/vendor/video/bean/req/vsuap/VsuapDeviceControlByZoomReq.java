package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-镜头变倍req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapDeviceControlByZoomReq {
    /**
     * 通道编码ID（必填）
     */
    private String channelCodeId;
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 镜头变倍。0：停止；1：缩小；2：放大（必填）
     */
    private String zoom;
    /**
     * 镜头变倍速度。取值0~15，值越大速度越快
     */
    private String zoomSpeed;
}
