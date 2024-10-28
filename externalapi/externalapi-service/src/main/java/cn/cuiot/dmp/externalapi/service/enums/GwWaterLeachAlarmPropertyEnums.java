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
    public enum waterLeachStateEnums {
        NORMAL("0", "正常"),
        WATER_LEACH("1", "水浸");

        @Getter
        private String code;
        @Getter
        private String desc;

        waterLeachStateEnums(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (waterLeachStateEnums e : waterLeachStateEnums.values()) {
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
    public enum powerSavingModeEnums {
        PSM("0", "正常"),
        DXR("1", "水浸"),
        E_DRX("2", "水浸"),
        NOT_OPENED("20", "未开通");


        @Getter
        private String code;
        @Getter
        private String desc;

        powerSavingModeEnums(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getNameByCode(String code) {
            if (StringUtils.isNotBlank(code)) {
                for (waterLeachStateEnums e : waterLeachStateEnums.values()) {
                    if (StringUtils.equals(e.getCode(), code)) {
                        return e.getDesc();
                    }
                }
            }
            return null;
        }
    }
}
