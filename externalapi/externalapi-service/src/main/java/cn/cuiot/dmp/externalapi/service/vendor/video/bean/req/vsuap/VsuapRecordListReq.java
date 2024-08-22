
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-查询设备云端录像信息req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class VsuapRecordListReq extends VsuapPageReq {
    /**
     * 通道编码ID (必填)
     */
    private String channelCodeId;
    /**
     * 设备ID (必填)
     */
    private String deviceId;
    /**
     * 范围查询的结束时间, 13位时间戳  (必填)
     * (开始时间与结束时间最大间隔7天)
     */
    private String endTime;
    /**
     * 范围查询的开始时间, 13位时间戳 (必填)
     */
    private String startTime;
    /**
     * 请求ID (非必填)
     */
    private String requestId;
}
