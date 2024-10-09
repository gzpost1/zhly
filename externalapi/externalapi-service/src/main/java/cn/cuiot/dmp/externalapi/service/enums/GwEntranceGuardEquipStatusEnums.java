package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 门禁设备状态
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwEntranceGuardEquipStatusEnums {

    /**
     * 在线
     */
    ON_LINE("0", "在线"),
    /**
     * 离线
     */
    OFF_LINE("1", "离线"),
    /**
     * 未激活
     */
    NOT_ACTIVE("2", "未激活"),
    ;

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    GwEntranceGuardEquipStatusEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
