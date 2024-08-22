package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-删除巡航组req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapCruiseDeleteReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 巡航组id（必填）
     */
    private String cruiseId;
}