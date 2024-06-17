package cn.cuiot.dmp.digitaltwin.base.exception;

import cn.cuiot.dmp.common.constant.IExceptionType;

/**
 * 格物消防返回码
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public enum GwFirefightErrorCode implements IExceptionType {
    /**
     * 成功
     */
    SUCCESS("200", "接收成功"),
    /**
     * 失败
     */
    ERROR("500", "接收失败");


    private String code;
    private String msg;

    private GwFirefightErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
