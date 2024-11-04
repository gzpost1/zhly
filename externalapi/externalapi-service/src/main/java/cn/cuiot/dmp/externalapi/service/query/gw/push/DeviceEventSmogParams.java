package cn.cuiot.dmp.externalapi.service.query.gw.push;

import lombok.Data;

/**
 * 格物烟雾报警器事件推送
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Data
public class DeviceEventSmogParams {

    /**
     * 电池电量上报
     */
    private BatteryInfo Battery;

    @Data
    public static class BatteryInfo {
        /**
         * 电池电量
         */
        private Integer batteryLevel;
        /**
         * 电池电压
         */
        private float batteryVoltage;
    }

    /**
     * 连接服务上报
     */
    private ConnectivityInfo Connectivity;


    @Data
    public static class ConnectivityInfo {
        /**
         * 信号强度
         */
        private Integer signalStrength;
        /**
         * 信号覆盖等级
         */
        private Integer signalECL;
        /**
         * 信噪比
         */
        private Integer signalSNR;
        /**
         * 连接质量
         */
        private float linkQuality;
        /**
         * 设备所在小区id
         */
        private Integer cellID;
        /**
         * 小区PCI
         */
        private Integer signalPCI;
    }

    /**
     * 故障上报
     */
    private AlarmInfo Alarm;


    @Data
    public static class AlarmInfo {
        /**
         * 告警状态
         */
        private Integer alarmCode;
        /**
         * 告警状态附带数据
         */
        private String alarmData;
    }

    /**
     * 设备信息
     */
    private Device DeviceInfo;

    @Data
    public static class Device {
        /**
         * 硬件版本号
         */
        private String hardwareVersion;
        /**
         * 软件版本号
         */
        private String softwareVersion;
        /**
         * 制造商
         */
        private String manufacturer;
        /**
         * 设备类型
         */
        private String deviceType;
        /**
         * 国际移动用户识别码
         */
        private String imsi;
        /**
         * 国际移动电话设备识别码
         */
        private String imei;
        /**
         * 集成电路卡识别码
         */
        private String iccid;
    }
}
