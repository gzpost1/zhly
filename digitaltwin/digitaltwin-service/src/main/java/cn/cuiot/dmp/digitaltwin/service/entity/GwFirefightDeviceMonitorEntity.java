package cn.cuiot.dmp.digitaltwin.service.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 格物消防-设备监测
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName(value = "tb_gw_firefight_device_monitor", autoResultMap = true)
public class GwFirefightDeviceMonitorEntity {
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
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<Map<String, Object>> outputParams;

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
