package cn.cuiot.dmp.digitaltwin.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 格物消防-实时报警
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName("tb_gw_firefight_realtime_alarm")
public class GwFirefightRealTimeAlarmEntity {
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
     * 火警/故障处 理状态
     */
    private String alarmStatus;

    /**
     * 区县编码
     */
    private String areaCode;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
