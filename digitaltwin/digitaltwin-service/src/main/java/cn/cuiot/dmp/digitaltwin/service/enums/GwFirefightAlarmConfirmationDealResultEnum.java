package cn.cuiot.dmp.digitaltwin.service.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 报警确认-处理状态
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
public enum GwFirefightAlarmConfirmationDealResultEnum {
    /**
     * 未处理
     */
    UNPROCESSED("0", "未处理"),

    /**
     * 处理中
     */
    PROCESSING("1", "处理中"),

    /**
     * 处理完成
     */
    PROCESSING_COMPLETED("2", "处理完成"),
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

    GwFirefightAlarmConfirmationDealResultEnum(String type, String desc) {
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
        for (GwFirefightAlarmConfirmationDealResultEnum e : GwFirefightAlarmConfirmationDealResultEnum.values()) {
            if (Objects.equals(e.getType(), type)) {
                return e.getDesc();
            }
        }
        return StringUtils.EMPTY;
    }
}
