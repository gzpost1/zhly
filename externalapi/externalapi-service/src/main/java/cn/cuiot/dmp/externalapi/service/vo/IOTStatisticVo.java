package cn.cuiot.dmp.externalapi.service.vo;

import lombok.Data;

/**
 * 物联网设备统计
 *
 * @Author: xiaotao
 * @Date: 2024-08-22
 */
@Data
public class IOTStatisticVo {

    /**
     * 门禁
     */
    private Long entranceGuard;

    /**
     * 水表
     */
    private Long waterMeter;

    /**
     * 电表
     */
    private Long electricityMeter;


    /**
     * 监控
     */
    private Long videoMeter;

}
