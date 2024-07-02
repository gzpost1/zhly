package cn.cuiot.dmp.lease.enums;


import java.util.Objects;

/**
 * 收费管理-消息通知
 *
 * @author zc
 */
public enum ChargeMsgTemplateEnum {
    /**
     * 客户通知单
     */
    CUSTOMER_NOTICE_TEMPLATE("123", "尊敬的客户您好，您在%s，%s账期已出，请在指定时间，及时缴费，如已缴费请忽略此信息。"),

    /**
     * 催款通知
     */
    COLLECTION_NOTICE_TEMPLATE("456", "尊敬的客户您好，您目前已有%s条费用，总计欠缴%s元，还请您及时缴费，如已完成缴费还请忽略此通知！"),
    ;


    /**
     * 短信模板id
     */
    private String templateId;
    /**
     * 通知内容
     */
    private String desc;

    ChargeMsgTemplateEnum(String templateId, String desc) {
        this.templateId = templateId;
        this.desc = desc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ChargeMsgTemplateEnum getByTemplateId(String templateId) {
        for (ChargeMsgTemplateEnum value : values()) {
            if (Objects.equals(value.templateId, templateId)) {
                return value;
            }
        }
        return null;
    }
}