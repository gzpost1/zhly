package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-设置预置位req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapPresetConfigReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 通道编码ID，当设备为nvr时必填
     */
    private String channelCodeId;
    /**
     * 预置位编号，1~25（必填）
     */
    private Integer presetId;
}
