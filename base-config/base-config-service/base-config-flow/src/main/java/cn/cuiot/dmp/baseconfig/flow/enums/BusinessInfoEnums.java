package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @author pengjian
 * @create 2024/5/15 9:17
 */
public enum BusinessInfoEnums {

    /**
     * 挂起
     */
    BUSINESS_PENDING((byte)0,"挂起"),
    /**
     * 超时
     */
    BUSINESS_TIME_OUT((byte)1,"超时"),

    /**
     * 评论
     */
    BUSINESS_COMMENT((byte)2,"评论"),

    /**
     * 督办
     */
    BUSINESS_HANDLE((byte)3,"督办"),

    /**
     * 终止
     */
    BUSINESS_CLOSE((byte)4,"终止"),

    /**
     * 转办
     */
    BUSINESS_TRANSFER((byte)5,"转办"),


    /**
     * 完成
     */
    BUSINESS_COMPLETED((byte)6,"完成"),

    /**
     * 拒绝
     */
    BUSINESS_REFUSE((byte)7,"拒绝"),

    /**
     * 回退
     */
    BUSINESS_ROLLBACK((byte)8,"回退"),

    /**
     * 发起
     */
    BUSINESS_START((byte)9,"发起"),

    /**
     * 审批同意
     */
    BUSINESS_AGREE((byte)10,"审批同意");
    private byte code;
    private String desc;

    BusinessInfoEnums(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
