package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @author pengjian
 * @create 2024/6/12 9:28
 */
public enum WorkSourceEnums {

    WORK_SOURCE_PLAN((byte)0,"计划生成"),

    WORK_SOURCE_MAKE((byte)1,"自查报事"),

    CUSTOMER_BILL_LADING((byte)2,"客户提单"),

    PROXY_CUSTOMER_RECORD((byte)3,"代录客单")
    ;

    private Byte code;

    private String desc;

    WorkSourceEnums(Byte code,String desc){
        code=code;
        desc=desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
