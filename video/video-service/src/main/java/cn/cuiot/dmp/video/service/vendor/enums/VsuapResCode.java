package cn.cuiot.dmp.video.service.vendor.enums;


import cn.cuiot.dmp.common.constant.ErrorCode;

import java.util.Objects;

/**
 * 云智眼-响应编码枚举
 *
 * @Author: zc
 * @Date: 2024-02-27
 */
public enum VsuapResCode {
    /**
     * 成功
     */
    SUCCESS(200, ErrorCode.SUCCESS.code(), "成功"),
    /**
     * 未知错误
     */
    UNKNOWN(-1, ErrorCode.UNKNOWN.code(), "未知错误"),
    ;
    private Integer code;
    private String errorCode;
    private String msg;

    private VsuapResCode(Integer code, String errorCode, String msg) {
        this.code = code;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static VsuapResCode getItemByCode(Integer code) {
        if (Objects.nonNull(code)) {
            VsuapResCode[] enumConstants = VsuapResCode.class.getEnumConstants();
            for (VsuapResCode item : enumConstants) {
                if (code.equals(item.getCode())) {
                    return item;
                }
            }
        }
        return UNKNOWN;
    }
}
