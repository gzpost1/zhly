package cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.enums.GwGasAlarmPropertyEnums;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 格物燃气报警器属性
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_gw_gas_alarm_property")
public class GwGasAlarmPropertyEntity extends YjBaseEntity {

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

    public static final String DEV_ID = "dev_id";
    public static final String DEV_TYPE = "dev_type";
    public static final String IMEI = "imei";
    public static final String ICCID = "iccid";
    public static final String SOFTVER = "softver";
    public static final String HARDVER = "hardver";
    public static final String SWIFT_NUM = "swift_num";
    public static final String DEV_NAME = "dev_name";
    public static final String MANUFACTURER = "manufacturer";
    public static final String DATE = "date";
    public static final String HEARTBEAT_TIME = "heartbeat_time";
    public static final String CONN_MODE = "CONN_MODE";
    public static final String RSRP = "rsrp";
    public static final String SENSOR1 = "sensor1";
    public static final String SENSOR2 = "sensor2";
    public static final String SENSOR_TYPE = "sensor_type";
    public static final String SENSOR_VALUE = "sensor_value";
    public static final String SENSOR_STATE = "sensor_state";
    public static final String SENSOR2_TYPE = "sensor2_type";
    public static final String SENSOR2_VALUE = "sensor2_value";
    public static final String SENSOR2_STATE = "sensor2_state";
    public static final String VALVE_STATE = "valve_state";
    public static final String RELAY_STATE = "relay_state";
    public static final String HIGH_ALARM = "high_alarm";
    public static final String ERROR_CODE = "error_code";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ALTITUDE = "altitude";
    public static final String POWER_SAVING_MODE = "powerSavingMode";
    public static final String SINR = "sinr";
    public static final String PCI = "pci";
    public static final String CELL_ID = "cell_id";
    public static final String ALARM_LIMIT = "AlarmLimit";
    public static final String NB_VERSION = "nbVersion";
    public static final String CONTROL_STATE = "control_state";
    public static final String HANDONOFF = "handonoff";
    public static final String MUTE_TIME_SET = "MuteTimeSet";
    public static final String MUTE = "mute";
    public static final String CHECK = "check";
    public static final String RESTART = "restart";
    public static final String GAS_VALUE = "gas_value";

    public static void main(String[] args) {
        System.out.println(JsonUtil.writeValueAsString(buildGwCommonPropertyVo(null)));
    }

    /**
     * 构建属性VO
     *
     * @Param deviceData JSON数据
     */
    public static List<GwCommonPropertyVo> buildGwCommonPropertyVo(GwGasAlarmPropertyEntity entity) {
        // 初始化属性列表
        List<GwCommonPropertyVo> vo = initializeProperties();

        // 检查实体和设备数据
        if (Objects.nonNull(entity) && StringUtils.isNotBlank(entity.deviceData)) {
            // 解析设备数据
            LinkedList<GwCommonPropertyVo> readValue = JsonUtil.readValue(entity.deviceData,
                    new TypeReference<LinkedList<GwCommonPropertyVo>>() {
                    });

            if (CollectionUtils.isNotEmpty(readValue)) {
                // 将解析结果转换为映射
                Map<String, GwCommonPropertyVo> map = readValue.stream().collect(Collectors.toMap(GwCommonPropertyVo::getKey, e -> e));

                // 更新属性值
                vo.forEach(item -> updatePropertyValue(item, map));

                // 处理传感器1和传感器2的数据
                processSensorData(map, vo, SENSOR1);
                processSensorData(map, vo, SENSOR2);
            }
        }
        return vo;
    }

    /**
     * 初始化属性列表的方法
     *
     * @return List
     */
    private static List<GwCommonPropertyVo> initializeProperties() {
        return Arrays.asList(
                new GwCommonPropertyVo(DATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(DATE)),
                new GwCommonPropertyVo(HEARTBEAT_TIME, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(HEARTBEAT_TIME)),
                new GwCommonPropertyVo(CONN_MODE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(CONN_MODE)),
                new GwCommonPropertyVo(RSRP, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(RSRP)),
                new GwCommonPropertyVo(SENSOR_TYPE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR_TYPE)),
                new GwCommonPropertyVo(SENSOR_VALUE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR_VALUE)),
                new GwCommonPropertyVo(SENSOR_STATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR_STATE)),
                new GwCommonPropertyVo(VALVE_STATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(VALVE_STATE)),
                new GwCommonPropertyVo(RELAY_STATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(RELAY_STATE)),
                new GwCommonPropertyVo(HIGH_ALARM, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(HIGH_ALARM)),
                new GwCommonPropertyVo(ERROR_CODE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(ERROR_CODE)),
                new GwCommonPropertyVo(POWER_SAVING_MODE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(POWER_SAVING_MODE)),
                new GwCommonPropertyVo(SENSOR2_TYPE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR2_TYPE)),
                new GwCommonPropertyVo(SENSOR2_VALUE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR2_VALUE)),
                new GwCommonPropertyVo(SENSOR2_STATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SENSOR2_STATE)),
                new GwCommonPropertyVo(SINR, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(SINR)),
                new GwCommonPropertyVo(ALARM_LIMIT, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(ALARM_LIMIT)),
                new GwCommonPropertyVo(NB_VERSION, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(NB_VERSION)),
                new GwCommonPropertyVo(CONTROL_STATE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(CONTROL_STATE)),
                new GwCommonPropertyVo(HANDONOFF, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(HANDONOFF)),
                new GwCommonPropertyVo(MUTE_TIME_SET, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(MUTE_TIME_SET)),
                new GwCommonPropertyVo(MUTE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(MUTE)),
                new GwCommonPropertyVo(CHECK, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(CHECK)),
                new GwCommonPropertyVo(GAS_VALUE, GwGasAlarmPropertyEnums.PropertyTitle.getNameByCode(GAS_VALUE))
        );
    }

    /**
     * 更新属性值的方法
     *
     * @return List
     */
    private static void updatePropertyValue(GwCommonPropertyVo item, Map<String, GwCommonPropertyVo> map) {
        // 如果映射中包含当前属性的键
        if (map.containsKey(item.getKey())) {
            GwCommonPropertyVo propertyVo = map.get(item.getKey());
            // 设置时间戳
            item.setTs(propertyVo.getTs());
            // 设置单位
            item.setUnit(propertyVo.getUnit());
            // 设置值
            item.setValue(propertyVo.getValue());

            // 根据属性键设置对应的枚举值
            switch (item.getKey()) {
                case CONN_MODE:
                    item.setValue(GwGasAlarmPropertyEnums.ConnMode.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case HIGH_ALARM:
                    item.setValue(GwGasAlarmPropertyEnums.AlarmSwitch.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case ERROR_CODE:
                    item.setValue(GwGasAlarmPropertyEnums.ErrorCode.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case POWER_SAVING_MODE:
                    item.setValue(GwGasAlarmPropertyEnums.PowerSavingMode.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case CONTROL_STATE:
                    item.setValue(GwGasAlarmPropertyEnums.ControlOutputType.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case HANDONOFF:
                    item.setValue(GwGasAlarmPropertyEnums.HandOnOff.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case MUTE:
                    item.setValue(GwGasAlarmPropertyEnums.Mute.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case CHECK:
                    item.setValue(GwGasAlarmPropertyEnums.SelfCheck.getNameByCode(propertyVo.getValue() + ""));
                    break;
                case RESTART:
                    item.setValue(GwGasAlarmPropertyEnums.Restart.getNameByCode(propertyVo.getValue() + ""));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 处理传感器数据的方法
     */
    private static void processSensorData(Map<String, GwCommonPropertyVo> map, List<GwCommonPropertyVo> vo, String sensorKey) {
        // 检查是否存在传感器数据
        if (map.containsKey(sensorKey)) {
            GwCommonPropertyVo propertyVo = map.get(sensorKey);
            if (Objects.nonNull(propertyVo.getValue())) {
                // 解析传感器数据
                LinkedList<GwCommonPropertyVo> vos = JsonUtil.readValue(propertyVo.getValue() + "",
                        new TypeReference<LinkedList<GwCommonPropertyVo>>() {
                        });

                if (CollectionUtils.isNotEmpty(vos)) {
                    // 将传感器数据转换为映射
                    Map<String, GwCommonPropertyVo> voMap = vos.stream().collect(Collectors.toMap(GwCommonPropertyVo::getKey, e -> e));
                    // 更新传感器属性
                    updateSensorProperties(vo, voMap, sensorKey);
                }
            }
        }
    }

    /**
     * 更新传感器属性的方法
     */
    private static void updateSensorProperties(List<GwCommonPropertyVo> vo, Map<String, GwCommonPropertyVo> voMap, String sensorKey) {
        String typeKey = sensorKey.equals(SENSOR1) ? SENSOR_TYPE : SENSOR2_TYPE;
        String valueKey = sensorKey.equals(SENSOR1) ? SENSOR_VALUE : SENSOR2_VALUE;
        String stateKey = sensorKey.equals(SENSOR1) ? SENSOR_STATE : SENSOR2_STATE;

        vo.forEach(item -> {
            // 更新传感器类型
            if (item.getKey().equals(typeKey) && voMap.containsKey(typeKey)) {
                GwCommonPropertyVo propertyVo1 = voMap.get(typeKey);
                item.setTs(propertyVo1.getTs());
                item.setUnit(propertyVo1.getUnit());
                item.setValue(GwGasAlarmPropertyEnums.SensorType.getNameByCode(propertyVo1.getValue() + ""));
            }
            // 更新传感器值
            if (item.getKey().equals(valueKey) && voMap.containsKey(valueKey)) {
                GwCommonPropertyVo propertyVo1 = voMap.get(valueKey);
                item.setTs(propertyVo1.getTs());
                item.setUnit(propertyVo1.getUnit());
                item.setValue(GwGasAlarmPropertyEnums.SensorState.getNameByCode(propertyVo1.getValue() + ""));
            }
            // 更新传感器状态
            if (item.getKey().equals(stateKey) && voMap.containsKey(stateKey)) {
                GwCommonPropertyVo propertyVo1 = voMap.get(stateKey);
                item.setTs(propertyVo1.getTs());
                item.setUnit(propertyVo1.getUnit());
                item.setValue(GwGasAlarmPropertyEnums.SensorState.getNameByCode(propertyVo1.getValue() + ""));
            }
        });
    }
}
