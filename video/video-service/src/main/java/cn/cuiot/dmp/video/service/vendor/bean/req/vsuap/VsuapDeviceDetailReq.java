package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-查询设备详情req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapDeviceDetailReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 请求ID（非必填）
     */
    private String requestId;
}
