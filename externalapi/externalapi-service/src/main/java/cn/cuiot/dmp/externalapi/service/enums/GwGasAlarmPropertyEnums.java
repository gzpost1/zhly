package cn.cuiot.dmp.externalapi.service.enums;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;

/**
 * 格物-水浸报警器属性枚举
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
public enum GwGasAlarmPropertyEnums {

    ALARM_PROPERTY_1;

    /**
     * 传感器类型
     */
    public enum SensorType {
        METHANE("1", "甲烷"),
        CARBON_MONOXIDE("4", "一氧化碳"),
        PROPANE("10", "丙烷"),
        TEMPERATURE("201", "温度");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        SensorType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (SensorType e : SensorType.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 传感器状态
     */
    public enum SensorState {
        PREHEAT("1", "预热"),
        NORMAL("2", "正常"),
        CALIBRATION_ERROR("3", "标定错误"),
        SENSOR_ERROR("4", "传感器错误"),
        WARNING("5", "预警"),
        LOW_REPORT("6", "低报"),
        HIGH_REPORT("7", "高报"),
        COMMUNICATION_FAILURE("8", "通讯故障"),
        OVER_LIMIT("9", "超量程"),
        TO_BE_CALIBRATED("10", "待校准"),
        HEARTBEAT_SIGNAL("11", "心跳信号"),
        SOS("12", "求救"),
        ABNORMAL_ALARM_AND_FAULT("13", "异常_报警和故障"),
        STEL_ALARM("14", "STEL报警"),
        TWA_ALARM("15", "TWA报警"),
        SELF_CHECK("16", "自检"),
        FAILURE("17", "失效"),
        POWER_LOSS("18", "掉电"),
        HIGH_TEMP_ALARM("19", "高温报警");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        SensorState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (SensorState e : SensorState.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 通讯模块
     */
    public enum ConnMode {

        NBIOT("0", "NBIOT"),
        CAT1("1", "CAT1"),
        G2("2", "2G"),
        G3("3", "3G"),
        G4("4", "4G"),
        G5("5", "5G"),
        WIFI("6", "WiFi"),
        ETHERNET("7", "Ethernet"),
        OTHER("8", "其他");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        ConnMode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (ConnMode e : ConnMode.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 脉冲阀
     */
    public enum ValveState {
        CLOSED("1", "阀闭合"),
        OPEN("2", "阀打开"),
        OPEN_CIRCUIT("3", "开路故障"),
        SHORT_CIRCUIT("4", "短路故障"),
        STUCK("5", "阀卡阻"),
        DISABLED("6", "功能禁用"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        ValveState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (ValveState e : ValveState.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 继电器
     */
    public enum RelayState {
        CLOSED("1", "继电器闭合"),
        OPEN("2", "继电器开路");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        RelayState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (RelayState e : RelayState.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 高温报警开关
     */
    public enum AlarmSwitch {
        ON("1", "开"),
        OFF("2", "关"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        AlarmSwitch(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (AlarmSwitch e : AlarmSwitch.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 故障
     */
    public enum ErrorCode {
        PREHEAT("1", "预热"),
        NORMAL("2", "正常"),
        CALIBRATION_ERROR("3", "标定错误"),
        SENSOR_ERROR("4", "传感器错误"),
        WARNING("5", "预警"),
        LOW_REPORT("6", "低报"),
        HIGH_REPORT("7", "高报"),
        COMMUNICATION_FAILURE("8", "通讯故障"),
        OVER_LIMIT("9", "超量程"),
        TO_BE_CALIBRATED("10", "待校准"),
        HEARTBEAT_SIGNAL("11", "心跳信号"),
        SOS("12", "求救"),
        ABNORMAL_ALARM_AND_FAULT("13", "异常_报警和故障"),
        STEL_ALARM("14", "STEL报警"),
        TWA_ALARM("15", "TWA报警"),
        SELF_CHECK("16", "自检"),
        FAILURE("17", "失效"),
        POWER_LOSS("18", "掉电"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        ErrorCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (ErrorCode e : ErrorCode.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 省电模式
     */
    public enum PowerSavingMode {
        PSM("0", "PSM"),
        DRX("1", "DRX"),
        EDRX("2", "eDRX"),
        NOT_ACTIVATED("20", "未开通"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        PowerSavingMode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (PowerSavingMode e : PowerSavingMode.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 控制输出类型
     */
    public enum ControlOutputType {
        MECHANICAL_ARM("0", "机械手"),
        SOLENOID_VALVE("1", "电磁阀");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        ControlOutputType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (ControlOutputType e : ControlOutputType.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 机械手状态
     */
    public enum HandOnOff {
        CLOSE("0", "关闭机械手"),
        OPEN("1", "打开机械手"),
        NOT_CONNECTED("2", "机械手未连接"),
        OPENING("3", "机械手正在打开"),
        CLOSING("4", "机械手正在关闭"),
        FAILURE("5", "机械手故障"),
        SOLENOID_VALVE_CLOSED("6", "电磁阀关"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        HandOnOff(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (HandOnOff e : HandOnOff.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 消音
     */
    public enum Mute {
        OFF("0", "关"),
        ON("1", "开");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        Mute(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (Mute e : Mute.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 自检
     */
    public enum SelfCheck {
        CHECK_ENDED("0", "自检结束"),
        START_CHECK("1", "开始自检");

        @Getter
        private final String code;
        @Getter
        private final String desc;

        SelfCheck(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (SelfCheck e : SelfCheck.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 重启
     */
    public enum Restart {
        RESTART("1", "重启"),
        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        Restart(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (Restart e : Restart.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 参数标题
     */
    public enum PropertyTitle {

        DEV_ID("dev_id","设备ID"),
        DEV_TYPE("dev_type","设备类型"),
        IMEI("IMEI","IMEI"),
        ICCID("ICCID","设备ICCID"),
        SOFTVER("softver","设备软件版本号"),
        HARDVER("hardver","设备硬件版本号"),
        SWIFT_NUM("swift_num","报文流水号"),
        DEV_NAME("dev_name","设备型号"),
        MANUFACTURER("manufacturer","设备厂商"),
        DATE("date","时间"),
        HEARTBEAT_TIME("heartbeat_time","心跳周期"),
        CONN_MODE("CONN_MODE","通讯模块"),
        RSRP("rsrp","接收信号强度"),
        SENSOR1("sensor1","传感器"),
        VALVE_STATE("valve_state","脉冲阀"),
        RELAY_STATE("relay_state","继电器"),
        HIGH_ALARM("high_alarm","高温报警开关"),
        ERROR_CODE("error_code","故障"),
        LONGITUDE("longitude","经度"),
        LATITUDE("latitude","纬度"),
        altitude("altitude","海拔"),
        POWERSAVINGMODE("powerSavingMode","省电模式"),
        SENSOR2("sensor2","传感器2"),
        SINR("sinr","信噪比"),
        PCI("PCI","物理小区标识"),
        CELLID("CELLID","小区ID"),
        ALARM_LIMIT("AlarmLimit","报警阈值"),
        NB_VERSION("nbVersion","通信模组版本号"),
        CONTROL_STATE("control_state","控制输出类型"),
        HANDONOFF("handonoff","机械手状态"),
        MUTETIMESET("MuteTimeSet","消音时长设置"),
        MUTE("mute","消音"),
        CHECK("check","自检"),
        RESTART("restart","重启"),
        GAS_VALUE("gas_value","气体浓度"),
        SENSOR_TYPE("sensor_type","传感器类型"),
        SENSOR_VALUE("sensor_value","传感器参数"),
        sensor_state("sensor_state","传感器状态"),
        SENSOR2_TYPE("sensor2_type","传感器2类型"),
        SENSOR2_VALUE("sensor2_value","传感器2参数"),
        sensor2_state("sensor2_state","传感器2状态"),

        ;

        @Getter
        private final String code;
        @Getter
        private final String desc;

        PropertyTitle(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (PropertyTitle e : PropertyTitle.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }
}
