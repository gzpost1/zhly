package cn.cuiot.dmp.externalapi.service.vendor.video.enums;

/**
 * AI算法类型
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
public enum VsuapMethodTypeEnum {

    /**
     * 统计类
     */
    STATISTICAL_CATEGORY(1, "统计类"),

    /**
     * 告警类
     */
    ALARM_CATEGORY(2, "告警类"),
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

    VsuapMethodTypeEnum(java.lang.Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
