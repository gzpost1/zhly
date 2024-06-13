package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @author pengjian
 * @create 2024/6/6 17:05
 */
public enum ButtonBusinessEnums {

    NOT_BUTTON((byte) 0,"不能点击"),
    BUTTON((byte) 1,"可以点击")
    ;
    private Byte code;

    private String desc;

    ButtonBusinessEnums(Byte code,String desc){
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
