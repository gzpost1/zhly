package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/9 16:26
 */
@Data
public class GateControlResultVO {

    /**
     * 通道id
     */
    private Integer nodeId;

    /**
     * 状态
     */
    private Integer status;
}
