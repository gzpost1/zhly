package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-查询统计类AI分析结果req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapAIStatisticsReq {
    /**
     * AI算法ID 统计类算法（必填）
     */
    private String methodId;
    /**
     * 设备ID（非必填）
     */
    private String deviceId;
    /**
     * 通道编码ID（非必填）
     */
    private String channelCodeId;
    /**
     * 查询类型 1：小时维度 2：天维度（必填）
     */
    private Integer queryType;
    /**
     * 范围查询的开始时间, 13位时间戳（必填）
     */
    private String startTime;
    /**
     * 范围查询的结束时间, 13位时间戳。开始时间与结束时间最大间隔7天 （必填）
     */
    private String endTime;
    /**
     * 请求ID（非必填）
     */
    private String requestId;
}
