package cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物水浸报警器属性
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_water_leach_alarm_property")
public class GwWaterLeachAlarmPropertyEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 水浸报警器id
     */
    @TableField(value = "gw_water_leach_alarm_id")
    private Long gwWaterLeachAlarmId;

    /**
     * 电池电量（百分比:0~100,单位%）
     */
    @TableField(value = "battery_level")
    private Integer batteryLevel;

    /**
     * 水浸状态（0正常，1水浸）
     */
    @TableField(value = "water_leach_state")
    private String waterLeachState;

    /**
     * 省电模式（0:PSM，1:DXR，2:eDRX，20:未开通）
     */
    @TableField(value = "power_saving_mode")
    private String powerSavingMode;

    /**
     * 信号强度
     */
    @TableField(value = "csq")
    private String csq;

    public static final String BATTERY_LEVEL = "battery_level";
    public static final String WATER_LEACH_STATE = "waterLeachState";
    public static final String POWER_SAVING_MODE = "powerSavingMode";
    public static final String CSQ = "CSQ";
}
