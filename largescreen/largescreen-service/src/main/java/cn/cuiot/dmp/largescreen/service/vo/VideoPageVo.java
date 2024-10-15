package cn.cuiot.dmp.largescreen.service.vo;

import lombok.Data;

/**
 * 监控后台-分页vo
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
@Data
public class VideoPageVo {

    /**
     * id
     */
    private Long id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备状态
     */
    private Integer state;

    /**
     * 设备状态名称
     */
    private String stateName;

    /**
     * flv流地址
     */
    private String flv;

    /**
     * hls流地址
     */
    private String hls;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;
}
