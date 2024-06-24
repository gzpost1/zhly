package cn.cuiot.dmp.digitaltwin.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 格物消防-设备信息
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName("tb_gw_firefight_device")
public class GwFirefightDeviceEntity {
    /**
     * id
     */
    @TableId("id")
    private Long id;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备 id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 所属账户 id
     */
    private String orgId;

    /**
     * 房间 id
     */
    private String siteId;

    /**
     * 房间名称
     */
    private String siteName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 国际移动设备身份码（IMEI）
     */
    private String imei;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     * 软件版本
     */
    private String softwareVersion;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

    /**
     * 地址
     */
    private String address;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 回路号
     */
    private String circuitCode;

    /**
     * 部位号
     */
    private String partCode;

    /**
     * 联系人列表
     */
    private String notifierList;

    /**
     * 单位信息
     */
    private String unitDto;

    /**
     * 建筑信息
     */
    private String architecturalDto;

    /**
     * 操作类型(A:新增；M:修改；D：删除（为“D”时，只传 id 和 imei，其他字段不传）)
     */
    private String action;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}