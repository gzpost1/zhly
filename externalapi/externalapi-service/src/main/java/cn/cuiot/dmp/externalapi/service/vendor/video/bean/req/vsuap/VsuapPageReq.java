package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import lombok.Data;

/**
 * 云智眼-公共分页参数
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Data
public class VsuapPageReq {
    /**
     * 分页查询页码, 分页查询时必填 (非必填)
     */
    private String index;

    /**
     * 分页查询行数, 默认10 (非必填)
     */
    private String rows;
}
