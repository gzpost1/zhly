package cn.cuiot.dmp.digitaltwin.service.enums;

/**
 * 设备状态
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
public enum GwFirefightDeviceEnum {
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
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    GwFirefightDeviceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
