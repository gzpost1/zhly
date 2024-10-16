package cn.cuiot.dmp.externalapi.service.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/15 16:24
 */
public enum AuthorizeEnums {


    AUTHORIZED((byte)1,"已授权"),

    UNAUTHORIZED((byte)0,"未授权"),
    ;


    private Byte code;

    private String name;

    AuthorizeEnums(Byte code,String name){
        this.code=code;
        this.name=name;
    }



    public static String getNameByCode(Byte code){
        for(AuthorizeEnums e:AuthorizeEnums.values()){
            if(Objects.equals(e.getCode(),code)){
                return e.getName();
            }
        }
        return null;
    }

    public Byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
