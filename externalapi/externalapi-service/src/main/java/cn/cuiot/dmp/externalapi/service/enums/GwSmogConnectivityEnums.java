package cn.cuiot.dmp.externalapi.service.enums;

/**
 * 烟雾报警器-连接服务上报
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public enum GwSmogConnectivityEnums {

    SIGNAL_STRENGTH("signalStrength", "信号强度","dBm"),

    SIGNAL_ECL("signalECL", "信号覆盖等级",""),

    SIGNAL_SNR("signalSNR", "信噪比","dB"),

    LINK_QUALITY("linkQuality", "连接质量","dB"),

    ;
    private String key;

    private String name;
    private String unit;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    GwSmogConnectivityEnums(String key, String name, String unit) {
        this.key = key;
        this.name = name;
        this.unit = unit;
    }
}
