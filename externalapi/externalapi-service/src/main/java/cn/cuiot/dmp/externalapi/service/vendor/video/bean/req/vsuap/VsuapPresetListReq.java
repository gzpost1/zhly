package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-预置位列表查询req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapPresetListReq {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 通道编码id, nvr时必填
     */
    private String channelCodeId;
}
