package cn.cuiot.dmp.digitaltwin.base.bean;

import cn.cuiot.dmp.digitaltwin.base.exception.GwFirefightErrorCode;

import java.io.Serializable;

/**
 * 格物消防-接口返回
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public class GwFirefightJsonResult implements Serializable {

    /**
     * 错误编号
     */
    private String code = "0";

    /**
     * 信息(如果发生错误，那么代表错误信息)
     */
    private String message = "";

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GwFirefightJsonResult(String code) {
        this.code = code;
    }

    public GwFirefightJsonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GwFirefightJsonResult success() {
        return new GwFirefightJsonResult(GwFirefightErrorCode.SUCCESS.getCode(), GwFirefightErrorCode.SUCCESS.getMessage());
    }

    public static GwFirefightJsonResult error() {
        return new GwFirefightJsonResult(GwFirefightErrorCode.ERROR.getCode(), GwFirefightErrorCode.ERROR.getMessage());
    }

    public static GwFirefightJsonResult error(String message) {
        return new GwFirefightJsonResult(GwFirefightErrorCode.ERROR.getCode(), message);
    }

    public static GwFirefightJsonResult error(GwFirefightErrorCode errorCode) {
        return new GwFirefightJsonResult(errorCode.getCode(), errorCode.getMessage());
    }
}
