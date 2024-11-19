package cn.cuiot.dmp.externalapi.service.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/15 16:10
 */
public enum PersonSexEnums {


    GENDER_MALE((byte)1,"男"),

    GENDER_FEMALE((byte)2,"女"),
            ;


    private Byte code;

    private String name;

    PersonSexEnums(Byte code,String name){
        this.code=code;
        this.name=name;
    }



    public static String getNameByCode(Byte code){
        for(PersonSexEnums e:PersonSexEnums.values()){
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
