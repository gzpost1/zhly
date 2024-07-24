package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @author pengjian
 * @create 2024/6/3 11:41
 */
public enum WorkOrderResultEnums {

    GENERATED((byte)0,"未生成"),

    NOT_GENERATED((byte)1,"已生成"),

    GENERATION_FAILED((byte)2,"生成失败")
    ;

    private byte code;

    private String desc;

    WorkOrderResultEnums(byte code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public byte getCode() {
        return code;
    }



    public String getDesc() {
        return desc;
    }

}
