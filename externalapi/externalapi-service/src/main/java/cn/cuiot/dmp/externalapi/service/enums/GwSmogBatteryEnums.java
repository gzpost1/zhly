package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 烟雾报警器-电量
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwSmogBatteryEnums {

    BATTERY_LEVEL("batteryLevel", "电池电量","%"),

    BATTERY_VOLTAGE("batteryVoltage", "电池电压","V"),

    ;
    private String key;

    private String name;
    private String unit;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    GwSmogBatteryEnums(String key, String name, String unit) {
        this.key = key;
        this.name = name;
        this.unit = unit;
    }
}
