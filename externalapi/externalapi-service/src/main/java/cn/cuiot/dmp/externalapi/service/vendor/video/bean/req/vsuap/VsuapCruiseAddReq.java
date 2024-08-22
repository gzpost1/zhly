package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-添加巡航组req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapCruiseAddReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 通道编码id,设备为nvr是必填（非必填）
     */
    private String channelCodeId;
    /**
     * 巡航组名称，40个字符以内（必填）
     */
    private String cruiseName;
    /**
     * 备注，256个字符以内（非必填）
     */
    private String remark;
}
