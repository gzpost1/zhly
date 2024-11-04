package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 烟雾报警器-属性
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwSmogPropertyEnums {

    SENSITIVITY("sensitivity", "灵敏度",GwSmogSensitivityEnums.class),

    SMOKE_CONCENTRATION("smokeConcentration", "烟雾浓度",null),

    TEMPERATURE("temperature", "环境温度",null),

    DBM_LIMIT("dbmLimit", "烟雾浓度报警阈值",null),

    TEMP_LIMIT("tempLimit", "温度报警阈值",null),

    SMOKE_DIRT("smokeDirt", "烟雾传感器污染度",null),

    OPERATION_CODE("operationCode", "设备操作",null),

    LONGITUDE("longitude", "经度",null),

    LATITUDE("latitude", "纬度",null),

    ALTITUDE("altitude", "海拔",null),

    POWER_SAVING_MODE("powerSavingMode", "省电模式",GwSmogPowerSavingModeEnums.class),

    REPORT_TIME("reportTime", "上报周期",null),
    ;

    private String key;

    private String name;
    private Class enumsClzz;

    public String getKey() {
        return key;
    }

    public void setSensitivity(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getEnumsClzz() {
        return enumsClzz;
    }

    public void setEnumsClzz(Class enumsClzz) {
        this.enumsClzz = enumsClzz;
    }

    GwSmogPropertyEnums(String key, String name, Class enumsClzz) {
        this.key = key;
        this.name = name;
        this.enumsClzz = enumsClzz;
    }

    public static String queryNameByKey(String key){
        for(GwSmogPropertyEnums statusEnums : GwSmogPropertyEnums.values()){
            if(statusEnums.getKey().equals(key)){
                return statusEnums.getName();
            }
        }
        return null;
    }

    public static GwSmogPropertyEnums queryEnumByKey(String key){
        for(GwSmogPropertyEnums statusEnums : GwSmogPropertyEnums.values()){
            if(statusEnums.getKey().equals(key)){
                return statusEnums;
            }
        }
        return null;
    }
}
