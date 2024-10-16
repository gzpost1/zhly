package cn.cuiot.dmp.pay.service.service.enums;

import lombok.Getter;

/**
 * 交易渠道：
 * 11：云闪付H5支付（云闪付环境内H5）
 * 10：支付宝H5支付（支付宝环境内H5）
 * 03：公众号支付
 * 04：H5支付
 * 05：APP支付
 * 06：native支付
 * 01：小程序支付
 *
 * @author huq
 */
public enum TradeChannelEnum {
    H5("04", "H5支付", "00250008", "h5"),
    WX_OPEN("03", "公众号支付", "00250006", "jsapi"),
    APP("05", "APP支付", "00250009", "app"),
    NATIVE("06", "native支付", "", "native"),
    MINI_APP("01", "小程序支付", "00250007", "jsapi"),
    ALIPAY("02", "支付宝生活号", "00250006", ""),
    QR_PAY("09", "二维码主扫支付", "", ""),
    PAYMENT_CODE_PAY("08", "二维码被扫支付", "", ""),
    ALIPAY_H5("10", "支付宝H5支付", "00250008", ""),
    UNION_H5("11", "云闪付H5支付", "00250008", ""),
    ;

    @Getter
    private String type;
    @Getter
    private String message;
    @Getter
    private String gBusiId;
    @Getter
    private String payCode;

    public static final String[] WECHAT_TRADE_CHANNELS = {WX_OPEN.getType(), NATIVE.getType(), MINI_APP.getType()};
    public static final String[] H5_TRADE_CHANNELS = {H5.getType(), APP.getType(), ALIPAY_H5.getType(), UNION_H5.getType()};

    TradeChannelEnum(String type, String message, String gBusiId, String payCode) {
        this.type = type;
        this.message = message;
        this.gBusiId = gBusiId;
        this.payCode = payCode;
    }

    public static String getName(String type) {
        if (type == null) {
            return MINI_APP.getMessage();
        }
        for (TradeChannelEnum statusEnum : TradeChannelEnum.values()) {
            if (type.equals(statusEnum.getType())) {
                return statusEnum.getMessage();
            }
        }
        return MINI_APP.getMessage();
    }

    public static String getGBusiId(String type) {
        if (type == null) {
            return MINI_APP.getGBusiId();
        }
        for (TradeChannelEnum statusEnum : TradeChannelEnum.values()) {
            if (type.equals(statusEnum.getType())) {
                return statusEnum.getGBusiId();
            }
        }
        return MINI_APP.getGBusiId();
    }

    public static String getPayCode(String type) {
        if (type == null) {
            return MINI_APP.getPayCode();
        }
        for (TradeChannelEnum statusEnum : TradeChannelEnum.values()) {
            if (type.equals(statusEnum.getType())) {
                return statusEnum.getPayCode();
            }
        }
        return MINI_APP.getPayCode();
    }
}
