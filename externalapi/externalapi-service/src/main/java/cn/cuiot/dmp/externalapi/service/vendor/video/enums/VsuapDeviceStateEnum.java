package cn.cuiot.dmp.externalapi.service.vendor.video.enums;

import java.util.Objects;

/**
 * 设备类型枚举
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
public enum VsuapDeviceStateEnum {

    /**
     * 未注册
     */
    UNREGISTERED(1, "未注册"),

    /**
     * 在线
     */
    ON_LINE(2, "在线"),

    /**
     * 离线
     */
    OFF_LINE(3, "离线"),
    ;

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    VsuapDeviceStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String queryDescByCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (VsuapDeviceStateEnum typeEnum : values()) {
            if (Objects.equals(typeEnum.getCode(), code)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
