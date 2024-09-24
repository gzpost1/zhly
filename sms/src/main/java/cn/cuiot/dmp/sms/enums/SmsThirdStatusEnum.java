package cn.cuiot.dmp.sms.enums;

import java.util.Objects;

/**
 * 第三方审核状态
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
public enum SmsThirdStatusEnum {
    /**
     * 未审核
     */
    UNAUDITED(0, "未审核"),
    /**
     * 审核成功
     */
    SUCCESS_AUDIT(1, "审核成功"),
    /**
     * 审核拒绝
     */
    AUDIT_REJECTION(2, "审核拒绝"),
    ;

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    SmsThirdStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByType(Integer code) {
        for (SmsThirdStatusEnum value : values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
