package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 业务保存类型
 * @Date 2024/4/28 15:41
 * @Created by libo
 */
public enum WorkBusinessEnums {
    //操作类型0挂起1超时
    SUSPEND((byte) 0, "挂起"),
    TIMEOUT((byte) 1, "超时"),
    CLOSE((byte) 4, "终止"),

    BUSINESS_AGREE((byte)10,"审批同意"),
    ;

    private Byte code;
    private String desc;

    WorkBusinessEnums(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }
}
