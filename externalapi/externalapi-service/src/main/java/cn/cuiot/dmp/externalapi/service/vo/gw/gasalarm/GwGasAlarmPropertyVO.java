package cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmPropertyEntity;
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
public class GwGasAlarmPropertyVO extends GwGasAlarmPropertyEntity {

    /**
     * 通讯模块名称
     */
    private String connModeName;

    /**
     * 传感器类型名称
     */
    private String sensorTypeName;

    /**
     * 传感器状态名称
     */
    private String sensorStateName;

    /**
     * 脉冲阀名称
     */
    private String valveStateName;

    /**
     * 继电器名称
     */
    private String relayStateName;

    /**
     * 高温报警开关名称
     */
    private String highAlarmName;

    /**
     * 故障名称
     */
    private String errorCodeName;

    /**
     * 省电模式名称
     */
    private String powerSavingModeName;

    /**
     * 控制输出类型名称
     */
    private String controlStateName;

    /**
     * 机械手状态名称
     */
    private String handonoffName;

    /**
     * 消音名称
     */
    private String muteName;

    /**
     * 自检名称
     */
    private String checkName;

    /**
     * 重启名称
     */
    private String restartName;
}
