package cn.cuiot.dmp.digitaltwin.service.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 报警确认-火警状态
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
public enum GwFirefightAlarmConfirmationFireResultEnum {
    /**
     * 一键报警
     */
    ONE_CLICK_ALARM("0", "一键报警"),

    /**
     * 报警处置
     */
    ALARM_HANDLING("1", "报警处置"),

    /**
     * 误报
     */
    FALSE_ALARM("2", "误报"),
    /**
     * 故障
     */
    FAULT("3", "故障"),
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

    GwFirefightAlarmConfirmationFireResultEnum(String type, String desc) {
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
        for (GwFirefightAlarmConfirmationFireResultEnum e : GwFirefightAlarmConfirmationFireResultEnum.values()) {
            if (Objects.equals(e.getType(), type)) {
                return e.getDesc();
            }
        }
        return StringUtils.EMPTY;
    }
}
