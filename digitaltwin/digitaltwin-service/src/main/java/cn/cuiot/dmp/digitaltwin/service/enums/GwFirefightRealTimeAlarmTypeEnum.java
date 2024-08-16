package cn.cuiot.dmp.digitaltwin.service.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 设备状态
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
public enum GwFirefightRealTimeAlarmTypeEnum {
    /**
     * 火警
     */
    ON_LINE("Alarm", "火警"),

    /**
     * 故障
     */
    OFF_LINE("PreAlarm", "故障"),

    /**
     * 恢复
     */
    NOT_ACTIVE("Resume", "恢复"),
    ;

    private String type;
    private String desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    GwFirefightRealTimeAlarmTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据value获取code
     */
    public static String getDescByType(String type) {
        if (StringUtils.isBlank(type)) {
            return StringUtils.EMPTY;
        }
        for (GwFirefightRealTimeAlarmTypeEnum e : GwFirefightRealTimeAlarmTypeEnum.values()) {
            if (Objects.equals(e.getType(), type)) {
                return e.getDesc();
            }
        }
        return StringUtils.EMPTY;
    }
}
