package cn.cuiot.dmp.externalapi.service.vo.video;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "设备ID", orderNum = "1", width = 20)
    private String deviceId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", orderNum = "0", width = 20)
    private String deviceName;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备类型名称
     */
    @Excel(name = "设备类型", orderNum = "3", width = 20)
    private String deviceTypeName;

    /**
     * 设备状态
     */
    private Integer state;

    /**
     * 设备状态名称
     */
    @Excel(name = "设备状态", orderNum = "4", width = 20)
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
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String buildingName;
}
