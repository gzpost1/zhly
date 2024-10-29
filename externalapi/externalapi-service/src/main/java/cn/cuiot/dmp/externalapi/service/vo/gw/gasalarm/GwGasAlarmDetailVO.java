package cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 格物-燃气报警器 详情VO
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
@Data
public class GwGasAlarmDetailVO {

    /**
     * id
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 企业ID
     */
    private Long deptId;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备IMEI
     */
    private String imei;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 1-启用 0-停用
     */
    private Byte status;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    private String equipStatus;

    /**
     * 省电模式（0:PSM；1:DRX；2:eDRX；20:未开通）
     */
    private String powerSavingMode;

    /**
     * 消音(0关；1开)
     */
    private String mute;

    /**
     * 消音时长设置(0~65535)
     */
    private Integer muteTimeSet;
}
