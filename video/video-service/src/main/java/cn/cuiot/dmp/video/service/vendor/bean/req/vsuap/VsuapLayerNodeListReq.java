package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-查询组织列表req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapLayerNodeListReq extends VsuapPageReq {
    /**
     * 上级组织ID
     */
    private String pLayerNodeId;

    /**
     * 是否只包含直属下级组织
     * 1 是
     * 0 否(默认)
     */
    private String isDirectly;
    
    /**
     * 请求ID
     */
    private String requestId;
}
