package cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.enums.GwWaterLeachAlarmPropertyEnums;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * 设备主键id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 设备属性json
     */
    @TableField(value = "device_data")
    private String deviceData;

    public static final String BATTERY_LEVEL = "batteryLevel";
    public static final String WATER_LEACH_STATE = "waterLeachState";
    public static final String POWER_SAVING_MODE = "powerSavingMode";
    public static final String CSQ = "CSQ";

    /**
     * 构建属性VO
     *
     * @Param deviceData JSON数据
     */
    public static List<GwCommonPropertyVo> buildGwCommonPropertyVo(GwWaterLeachAlarmPropertyEntity entity) {

        List<GwCommonPropertyVo> vo = Lists.newLinkedList();
        vo.add(new GwCommonPropertyVo(BATTERY_LEVEL, GwWaterLeachAlarmPropertyEnums.PropertyTitle.getNameByCode(BATTERY_LEVEL)));
        vo.add(new GwCommonPropertyVo(WATER_LEACH_STATE, GwWaterLeachAlarmPropertyEnums.PropertyTitle.getNameByCode(WATER_LEACH_STATE)));
        vo.add(new GwCommonPropertyVo(POWER_SAVING_MODE, GwWaterLeachAlarmPropertyEnums.PropertyTitle.getNameByCode(POWER_SAVING_MODE)));
        vo.add(new GwCommonPropertyVo(CSQ, GwWaterLeachAlarmPropertyEnums.PropertyTitle.getNameByCode(CSQ)));

        if (Objects.nonNull(entity) && StringUtils.isNotBlank(entity.getDeviceData())) {
            LinkedList<GwCommonPropertyVo> readValue = JsonUtil.readValue(entity.getDeviceData(),
                    new TypeReference<LinkedList<GwCommonPropertyVo>>() {
                    });

            if (CollectionUtils.isNotEmpty(readValue)) {
                Map<String, GwCommonPropertyVo> map = readValue.stream().collect(Collectors.toMap(GwCommonPropertyVo::getKey, e -> e));
                vo.forEach(item -> {
                    if (map.containsKey(item.getKey())) {
                        GwCommonPropertyVo propertyVo = map.get(item.getKey());
                        String code = propertyVo.getValue() + "";

                        if (Objects.equals(item.getKey(), WATER_LEACH_STATE)) {
                            // 设置水浸状态值
                            item.setValue(GwWaterLeachAlarmPropertyEnums.WaterLeachState.getNameByCode(code));
                        }
                        if (Objects.equals(item.getKey(), POWER_SAVING_MODE)) {
                            // 设置省电模式值
                            item.setValue(GwWaterLeachAlarmPropertyEnums.PowerSavingMode.getNameByCode(code));
                        }

                        item.setTs(propertyVo.getTs());
                        item.setUnit(propertyVo.getUnit());
                        item.setValue(propertyVo.getValue());
                    }
                });
            }
        }
        return vo;
    }
}
