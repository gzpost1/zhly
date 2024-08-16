package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-查询告警类AI分析结果req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapAIAlarmListReq extends VsuapPageReq {
    /**
     * AI算法ID 告警类算法（非必填）
     */
    private String methodId;
    /**
     * 设备ID （非必填）
     */
    private String deviceId;
    /**
     * 通道编码ID （非必填）
     */
    private String channelCodeId;
    /**
     * 范围查询的开始时间, 13位时间戳 （必填）
     */
    private String startTime;
    /**
     * 范围查询的结束时间, 13位时间戳 （必填）
     */
    private String endTime;
    /**
     * 请求ID
     */
    private String requestId;
}
