package cn.cuiot.dmp.lease.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/13 21:10
 */
public enum ChannelEnum {
    SYSTEM_MESSAGE((byte) 1, "系统消息"),
    SMS_MESSAGE((byte) 2, "短息");

    private Byte code;
    private String desc;

    ChannelEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(Byte code){
        for(ChannelEnum chargeAbrogateEnum:ChannelEnum.values()){
            if(Objects.equals(code,chargeAbrogateEnum.getCode())){
                return chargeAbrogateEnum.getDesc();
            }
        }
        return null;
    }
    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
