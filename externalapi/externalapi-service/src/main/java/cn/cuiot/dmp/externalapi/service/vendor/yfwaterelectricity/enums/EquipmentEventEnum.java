package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.enums;


import lombok.Getter;

/**
 * 设备事件
 * @Author: xiaotao
 * @Date: 2024-09-29
 */
@Getter
public enum EquipmentEventEnum {

    /**
     * 获取实时数据
     */
    GET_REMAIN_TOTAL(1,"getRemainTotal"),


    /**
     * 获取小时数据
     */
    GET_HOUR_DATA(2,"getHourData"),


    /**
     * 获取日用量数据
     */
    GET_DAY_DATA(3,"getDayData"),



    /**
     * 获取月用量数据
     */
    GET_MONTH_DATA(4,"getMonthData");


    private Integer code;
    private String desc;


    EquipmentEventEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
