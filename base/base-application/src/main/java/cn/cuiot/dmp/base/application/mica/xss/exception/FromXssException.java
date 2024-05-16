package cn.cuiot.dmp.base.application.mica.xss.exception;

import lombok.Getter;

/**
 * xss 表单异常
 * @author: wuyongchong
 * @date: 2024/5/16 10:28
 */
@Getter
public class FromXssException extends IllegalStateException implements XssException {
    private final String input;

    public FromXssException(String input, String message) {
        super(message);
        this.input = input;
    }
}