package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-巡航点列表查询resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapCruiseConfigListResp {
    /**
     * 巡航组id
     */
    private String cruiseId;
    /**
     * 巡航点id
     */
    private String cruiseConfigId;
    /**
     * 预置位id
     */
    private Integer presetId;
    /**
     * 巡航点停留时长，单位秒
     */
    private Integer stopTime;
    /**
     * 备注
     */
    private String remark;
}
