package cn.cuiot.dmp.pay.service.service.enums;

import lombok.Getter;

/**
 * 01:工银e支付 02:微信支付 03:支付宝 04:预付卡 05:转账 06:二维码主扫支付 07:POS支付 08:e支付有协议小额免密 09:微信线下支付 10:会员卡小额免密 11:支付宝线下支付
 * * 12:二维码被扫支付 15:全额优惠 16:融资支付 17:云闪付 20:余额 99:其他
 */
public enum AppPayMethodEnum {
    ICBC("01", "工银e支付", ""),
    WECHAT("02", "微信支付", "wxpay-MWEB"),
    ALIPAY("03", "支付宝", "alipay-MWEB"),
    GNETE("17", "云闪付", "cuppay-MWEB"),
    BALANCE("20", "会员余额", "balance-MWEB"),
    OTHERS("99", "其他", "others"),
    ;
    public static final String[] APP_PAY_METHOD_LSIT = {WECHAT.getPayMethod(), ICBC.getPayMethod(),
            ALIPAY.getPayMethod(),
            GNETE.getPayMethod()};
    private static final String ALIPAY_H5 = "alipay-JSAPI";
    private static final String GNETE_H5 = "cuppay-JSAPI";
    @Getter
    private String payMethod;
    @Getter
    private String message;
    /**
     * app支付环境下的交易渠道
     * APP：微信支付
     * APP-MINI：云闪付
     * APP-MINI：支付宝
     */
    @Getter
    private String gPayMethod;

    AppPayMethodEnum(String payMethod, String message, String gPayMethod) {
        this.payMethod = payMethod;
        this.message = message;
        this.gPayMethod = gPayMethod;
    }



}
