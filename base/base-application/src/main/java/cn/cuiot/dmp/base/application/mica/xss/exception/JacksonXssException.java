package cn.cuiot.dmp.base.application.mica.xss.exception;

import java.io.IOException;
import lombok.Getter;

/**
 * xss jackson 异常
 * @author: wuyongchong
 * @date: 2024/5/16 10:28
 */
@Getter
public class JacksonXssException extends IllegalStateException implements XssException {
    private final String name;
    private final String input;

    public JacksonXssException(String name, String input, String message) {
        super(message);
        this.name = name;
        this.input = input;
    }

}