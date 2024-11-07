package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 门禁设备状态
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwSmogSensitivityEnums {

    /**
     * 低灵敏度
     */
    LOW("1", "低灵敏度"),
    /**
     * 普通
     */
    GENERAL("2", "普通"),
    /**
     * 高灵敏度
     */
    HIGH("3", "高灵敏度"),
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

    GwSmogSensitivityEnums(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String queryNameByValue(String value){
        for(GwSmogSensitivityEnums statusEnums : GwSmogSensitivityEnums.values()){
            if(statusEnums.getValue().equals(value)){
                return statusEnums.getName();
            }
        }
        return null;
    }
}
