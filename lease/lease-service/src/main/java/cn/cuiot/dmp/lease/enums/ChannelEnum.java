package cn.cuiot.dmp.lease.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/13 21:10
 */
public enum ChannelEnum {
    SYSTEM_MESSAGE("1", "微信"),
    SMS_MESSAGE("2", "短信");

    private String code;
    private String desc;

    ChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code){
        for(ChannelEnum chargeAbrogateEnum:ChannelEnum.values()){
            if(Objects.equals(code,chargeAbrogateEnum.getCode())){
                return chargeAbrogateEnum.getDesc();
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
