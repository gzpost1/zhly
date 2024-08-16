package cn.cuiot.dmp.digitaltwin.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 格物消防-设备监测
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_gw_firefight_device_monitor", autoResultMap = true)
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
     * 上报日期
     */
    private LocalDate reportDate;

    /**
     * 设备检测数据，json字符串
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<Map<String, Object>> outputParams;

    /**
     * 区县编码
     */
    private String areaCode;
}
