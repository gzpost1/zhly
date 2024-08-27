package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询组织详情resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapLayerNodeDetailResp {
    /**
     * 组织ID
     */
    private String layerNodeId;
    /**
     * 组织名称
     */
    private String layerNodeName;
    /**
     * 组织层级
     */
    private String tierLevel;
    /**
     * 上级组织ID
     */
    private Integer pLayerNodeId;
    /**
     * 上级组织名称全称
     */
    private String pLayerNodeNames;
    /**
     * 组织描述
     */
    private String description;
    /**
     * 创建时间, 13位时间戳
     */
    private Long createTime;
}
