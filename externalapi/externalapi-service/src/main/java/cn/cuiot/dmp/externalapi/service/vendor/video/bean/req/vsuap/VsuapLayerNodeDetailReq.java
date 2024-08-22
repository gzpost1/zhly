package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-查询组织详情req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapLayerNodeDetailReq {
    /**
     * 组织ID
     */
    private String layerNodeId;

    /**
     * 请求ID
     */
    private String requestId;
}
