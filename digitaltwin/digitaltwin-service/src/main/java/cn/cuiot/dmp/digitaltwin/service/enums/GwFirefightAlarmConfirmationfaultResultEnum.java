package cn.cuiot.dmp.digitaltwin.service.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 报警确认-故障状态
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
public enum GwFirefightAlarmConfirmationfaultResultEnum {
    /**
     * 确认故障
     */
    CONFIRM_THE_FAULT("1", "确认故障"),

    /**
     * 误报
     */
    FALSE_ALARM("2", "误报"),

    /**
     * 转派工单
     */
    TRANSFER_WORK_ORDER("3", "转派工单"),
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

    GwFirefightAlarmConfirmationfaultResultEnum(String type, String desc) {
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
        for (GwFirefightAlarmConfirmationfaultResultEnum e : GwFirefightAlarmConfirmationfaultResultEnum.values()) {
            if (Objects.equals(e.getType(), type)) {
                return e.getDesc();
            }
        }
        return StringUtils.EMPTY;
    }
}
