package cn.cuiot.dmp.externalapi.service.vendor.video.enums;

import java.util.Objects;

/**
 * 设备类型枚举
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
public enum VsuapDeviceTypeEnum {

    /**
     * 国标摄像头
     */
    NATIONAL_STANDARD_CAMERA(1, "国标摄像头"),

    /**
     * RTMP摄像头
     */
    RTMP_CAMERA(2, "RTMP摄像头"),

    /**
     * 国标平台
     */
    NATIONAL_STANDARD_PLATFORM(3, "国标平台"),

    /**
     * 设备
     */
    NVR(4, "设备"),
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

    VsuapDeviceTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String queryDescByCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (VsuapDeviceTypeEnum typeEnum : values()) {
            if (Objects.equals(typeEnum.getCode(), code)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
