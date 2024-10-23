package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 烟雾报警器-省电模式
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwSmogPowerSavingModeEnums {

    /**
     * PSM
     */
    PSM("0", "PSM"),

    /**
     * DRX
     */
    DRX("1", "DRX"),
    /**
     * eDRX
     */
    eDRX("2", "eDRX"),
    /**
     * 未开通
     */
    NOT_ACTIVATED("20", "未开通"),
    ;

    private String value;

    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    GwSmogPowerSavingModeEnums(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String queryNameByValue(String value){
        for(GwSmogPowerSavingModeEnums statusEnums : GwSmogPowerSavingModeEnums.values()){
            if(statusEnums.getValue().equals(value)){
                return statusEnums.name();
            }
        }
        return null;
    }
}
