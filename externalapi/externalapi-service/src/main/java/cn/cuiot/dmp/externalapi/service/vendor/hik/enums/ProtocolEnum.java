package cn.cuiot.dmp.externalapi.service.vendor.hik.enums;

/**
 * 网络请求协议
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
public enum ProtocolEnum {
    /**
     * http协议
     */
    HTTP("http://"),

    /**
     * https协议
     */
    HTTPS("https://");

    ProtocolEnum(String get) {
        this.get = get;
    }

    private String get;

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }
}
