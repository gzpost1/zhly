package cn.cuiot.dmp.base.application.mica.xss.exception;

/**
 * xss 异常，校验模式抛出
 * @author: wuyongchong
 * @date: 2024/5/16 10:27
 */
public class XssException extends RuntimeException{

    public XssException(String message) {
        super(message);
    }
}
