package cn.cuiot.dmp.base.infrastructure.xxljob;

import java.io.Serializable;

/**
 * common return
 *
 * @author xuxueli 2015-12-4 16:32:31
 */
public class XxlReturnT<T> implements Serializable {

    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final XxlReturnT<String> SUCCESS = new XxlReturnT<String>(null);
    public static final XxlReturnT<String> FAIL = new XxlReturnT<String>(FAIL_CODE, null);

    private int code;
    private String msg;
    private T content;

    public XxlReturnT() {
    }

    public XxlReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public XxlReturnT(T content) {
        this.code = SUCCESS_CODE;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "XxlReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
    }

}
