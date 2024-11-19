package cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物水浸报警器故障记录
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_water_leach_alarm_fault_record")
public class GwWaterLeachAlarmFaultRecordEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 水浸报警器id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 电池电量（百分比:0~100,单位%）
     */
    @TableField(value = "error_code")
    private String errorCode;
}
