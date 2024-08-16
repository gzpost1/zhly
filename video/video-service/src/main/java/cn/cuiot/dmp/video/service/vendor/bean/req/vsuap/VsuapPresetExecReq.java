package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-调用预置位req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapPresetExecReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 通道编码id，nvr设备必填（非必填）
     */
    private String channelCodeId;
    /**
     * 预置位编号，1~255（必填）
     */
    private Integer presetId;
}
