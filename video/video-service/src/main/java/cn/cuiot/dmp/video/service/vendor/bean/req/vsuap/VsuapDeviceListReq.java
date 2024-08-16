package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-查询设备列表req
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class VsuapDeviceListReq extends VsuapPageReq {
    /**
     * 组织ID（非必填）
     */
    private String layerNodeId;

    /**
     * 是否只包含直属下级设备（非必填）
     * 1 是
     * 0 否(默认)
     */
    private String isDirectly;

    /**
     * 请求ID（非必填）
     */
    private String requestId;
}
