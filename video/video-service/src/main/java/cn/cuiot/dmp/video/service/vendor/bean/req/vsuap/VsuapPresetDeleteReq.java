
package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-删除预置位req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapPresetDeleteReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 预置位编号，1~255（必填）
     */
    private Integer presetId;
    /**
     * 通道编码id，当为nvr时必填（非必填）
     */
    private String channelCodeId;
}
