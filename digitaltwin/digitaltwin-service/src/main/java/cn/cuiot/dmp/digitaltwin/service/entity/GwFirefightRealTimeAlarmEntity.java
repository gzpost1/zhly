package cn.cuiot.dmp.digitaltwin.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;

/**
 * 格物消防-实时报警
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_firefight_realtime_alarm")
public class GwFirefightRealTimeAlarmEntity extends YjBaseEntity {
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
     * 事件类别
     */
    private String type;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 上报时间
     */
    private Date reportTime;

    /**
     * 上报日期
     */
    private LocalDate reportDate;

    /**
     * 火警/故障处 理状态
     */
    private String alarmStatus;

    /**
     * 区县编码
     */
    private String areaCode;
}
