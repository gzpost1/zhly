package cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmPropertyEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 水浸报警器属性
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwWaterLeachAlarmPropertyVO extends GwWaterLeachAlarmPropertyEntity {

    /**
     * 电池电量名称
     */
    private String batteryLevelName;

    /**
     * 水浸状态名称
     */
    private String waterLeachStateName;

    /**
     * 省电模式名称
     */
    private String powerSavingModeName;
}
