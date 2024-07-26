package cn.cuiot.dmp.digitaltwin.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 格物消防-设备监测
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_firefight_device_monitor")
public class GwFirefightDeviceMonitorEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId("id")
    private Long id;

    /**
     * 消息编号
     */
    private String messageId;

    /**
     * 设备 id
     */
    private String deviceId;

    /**
     * 设备编码
     */
    private String imei;

    /**
     * 上报时间
     */
    private Date reportTime;

    /**
     * 设备检测数据，json字符串
     */
    private String outputParams;

    /**
     * 区县编码
     */
    private String areaCode;
}
