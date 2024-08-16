package cn.cuiot.dmp.video.service.vendor.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-巡航组列表查询req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapCruiseListReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 巡航组id（必填）
     */
    private String cruiseId;
}
