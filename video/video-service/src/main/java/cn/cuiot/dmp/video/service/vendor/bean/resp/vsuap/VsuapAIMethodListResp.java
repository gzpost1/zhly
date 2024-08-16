
package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询AI算法列表resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapAIMethodListResp {
    /**
     * AI算法ID
     */
    private String methodId;
    /**
     * AI算法名称
     */
    private String methodName;
    /**
     * 算法类型（1：统计类 2：告警类 ）
     */
    private Integer type;

}
