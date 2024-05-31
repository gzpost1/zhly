package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 业务保存类型
 * @Date 2024/4/28 15:41
 * @Created by libo
 */
public enum WorkBusinessEnums {
    //操作类型0挂起1超时
    SUSPEND((byte) 0, "挂起","审批超时，系统自动挂起"),
    TIMEOUT((byte) 1, "超时",""),
    CLOSE((byte) 4, "终止","审批超时，系统自动终止"),

    BUSINESS_AGREE((byte)10,"审批同意","审批超时，系统自动通过"),
    ;

    private Byte code;
    private String desc;

    private String message;

    WorkBusinessEnums(Byte code, String desc, String message) {
        this.code = code;
        this.desc = desc;
        this.message = message;
    }

    public Byte getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }
}
