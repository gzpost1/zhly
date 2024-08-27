
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-修改巡航点req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapCruiseConfigModifyReq {
    /**
     * 设备ID（必填）
     */
    private String deviceId;
    /**
     * 巡航点id（必填）
     */
    private String cruiseConfigId;
    /**
     * 巡航点停留时间，单位秒 30~1800（必填）
     */
    private long stopTime;
    /**
     * 备注，256个字符以内
     */
    private String remark;
}
