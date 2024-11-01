package cn.cuiot.dmp.externalapi.service.enums;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;

/**
 * 格物-水浸报警器属性枚举
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
public enum GwWaterLeachAlarmPropertyEnums {

    ALARM_PROPERTY_1;

    /**
     * 水浸状态
     */
    public enum WaterLeachState {
        NORMAL("0", "正常"),
        WATER_LEACH("1", "水浸");

        @Getter
        private String code;
        @Getter
        private String desc;

        WaterLeachState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (WaterLeachState e : WaterLeachState.values()) {
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
        PSM("0", "正常"),
        DXR("1", "水浸"),
        E_DRX("2", "水浸"),
        NOT_OPENED("20", "未开通");


        @Getter
        private String code;
        @Getter
        private String desc;

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
     * 故障
     */
    public enum ErrorCode {
        NORMAL("0", "无故障"),
        FAULT("1", "故障")
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
                for (GwWaterLeachAlarmPropertyEnums.ErrorCode e : GwWaterLeachAlarmPropertyEnums.ErrorCode.values()) {
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

        BATTER_YLEVEL("batteryLevel","电池电量"),
        WATER_LEACH_STATE("waterLeachState","水浸状态"),
        POWER_SAVING_MODE("powerSavingMode","省电模式"),
        CSQ("CSQ","信号强度"),
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
                for (GwWaterLeachAlarmPropertyEnums.PropertyTitle e : GwWaterLeachAlarmPropertyEnums.PropertyTitle.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }
}
