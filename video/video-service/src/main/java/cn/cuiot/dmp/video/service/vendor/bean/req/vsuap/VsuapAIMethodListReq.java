package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-查询AI算法列表req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapAIMethodListReq extends VsuapPageReq {
    /**
     * 请求ID（非必填）
     */
    private String requestId;
    /**
     * 算法类型（1：统计类 2：告警类 （非必填）
     */
    private Integer type;
}
