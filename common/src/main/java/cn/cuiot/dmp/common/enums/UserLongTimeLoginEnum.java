package cn.cuiot.dmp.common.enums;

/**
 * @author Wangqd
 * @className UserLongTimeLoginEnum
 * @description 用户长期登录枚举类
 * @date 2023-03-16
 */
public enum UserLongTimeLoginEnum {

    /**
     * 开启长期登录
     */
    OPEN("1", "开启"),
    /**
     *关闭长期登录
     */
    CLOSE("0", "关闭");


    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    UserLongTimeLoginEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
