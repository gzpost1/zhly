package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 云智眼-云智眼-查询设备通道列表req
 *
 * @author zc
 * @Param:
 * @return:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class VsuapChannelReq extends VsuapPageReq {
    /**
     * 设备ID (必填)
     */
    private String deviceId;
    /**
     * 请求ID (非必填)
     */
    private String requestId;
}
