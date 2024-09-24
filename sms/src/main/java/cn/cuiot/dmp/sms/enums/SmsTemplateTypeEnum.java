package cn.cuiot.dmp.sms.enums;

import java.util.Objects;

/**
 * 模板类型
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
public enum SmsTemplateTypeEnum {
    /**
     * 验证码
     */
    CAPTCHA((byte) 1, "验证码"),
    /**
     * 公告通知
     */
    NOTICE((byte) 2, "公告通知"),
    /**
     * 线索通知
     */
    CLUE_NOTICE((byte) 3, "线索通知"),
    /**
     * 工单通知
     */
    WORK_INFO_NOTICE((byte) 4, "工单通知"),
    /**
     * 催缴通知
     */
    CHARGE_COLLECTION_NOTICE((byte) 5, "催缴通知"),
    /**
     * 账单通知
     */
    CHARGE_BILL_NOTICE((byte) 6, "账单通知"),
    ;

    private Byte code;
    private String desc;

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    SmsTemplateTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByType(Byte smsType) {
        for (SmsTemplateTypeEnum value : values()) {
            if (Objects.equals(value.getCode(), smsType)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
