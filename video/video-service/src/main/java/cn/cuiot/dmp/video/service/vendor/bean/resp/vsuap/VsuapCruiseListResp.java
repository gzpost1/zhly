
package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-巡航组列表查询resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapCruiseListResp {
    /**
     * 巡航组id
     */
    private String cruiseId;
    /**
     * 巡航组名称
     */
    private String cruiseName;
    /**
     * 巡航状态 0:未巡航 1：巡航中
     */
    private long cruiseState;
    /**
     * 备注
     */
    private String remark;
}
