package cn.cuiot.dmp.externalapi.service.enums;

import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 水表阀门状态
 * @author pengjian
 * @create 2024/9/6 16:01
 */
public enum WaterMeterEnums {

    OPEN_VALUE("0","开阀"),

    CLOSE_VALUE("1","关阀"),
    ;


    private String code;

    private String name;

    WaterMeterEnums(String code,String name){
        this.code=code;
        this.name=name;
    }

    public static String getNameByCode(String code){
        for(WaterMeterEnums e:WaterMeterEnums.values()){
            if(StringUtils.equals(e.getCode(),code)){
                return e.getName();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
