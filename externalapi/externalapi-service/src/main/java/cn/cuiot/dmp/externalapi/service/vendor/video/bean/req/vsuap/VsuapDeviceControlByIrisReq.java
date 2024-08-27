
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-镜头光圈控制req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapDeviceControlByIrisReq {
    /**
     * 通道编码ID（必填）
     */
    private String channelCodeId;
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 镜头光圈控制。0：停止；1：缩小；2：放大（必填）
     */
    private String iris;
    /**
     * 镜头光圈控制速度。取值0~255，值越大速度越快（必填）
     */
    private String irisSpeed;
}
