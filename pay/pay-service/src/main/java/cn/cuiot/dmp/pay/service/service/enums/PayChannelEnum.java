package cn.cuiot.dmp.pay.service.service.enums;

import cn.cuiot.dmp.pay.service.service.vo.PayChannelMark;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

public enum PayChannelEnum {
    WECHAT_NORMAL(1, "普通商户", Byte.parseByte("1"), new PayChannelMark("1_1"), "微信支付", false),
    WECHAT_NORMAL_SERVICE(1, "服务商-特约商户", Byte.parseByte("0"), new PayChannelMark("1_0"), "微信支付", false),
    WECHAT_COMBINE(1, "服务商（收付通）- 二级商户", Byte.parseByte("2"), new PayChannelMark("1_2"), "微信支付", false),
    ALIPAY(2, "支付宝", Byte.parseByte("3"), new PayChannelMark("2_3"), "支付宝支付", false),
    GNETE(4, "网付通", Byte.parseByte("3"), new PayChannelMark("4_3"), "网付通支付", true),
    ICBC(8, "工行", Byte.parseByte("3"), new PayChannelMark("8_3"), "工行支付", false),
    UNIONPAY(16, "银联", Byte.parseByte("3"), new PayChannelMark("16_3"), "银联支付", false),
    BALANCE(20, "会员余额支付", Byte.parseByte("3"), new PayChannelMark("20_3"), "会员余额支付", false),
    DEFAULT_MCH_TYPE(-1, "默认", Byte.parseByte("3"), new PayChannelMark(), "默认", false),
    ;
    /**
     *
     */
    @Getter
    private Integer payChannel;
    /**
     * （支付渠道+商户模式）名称
     */
    @Getter
    private String message;
    @Getter
    private Byte mchType;
    /**
     * 服务标识
     */
    @Getter
    private PayChannelMark mark;
    /**
     * 支付渠道名称
     */
    @Getter
    private String payChannelName;
    /**
     * 支付手续费支出账户是否为平台
     */
    @Getter
    private Boolean returnPayChargePlatform;

    PayChannelEnum(Integer payChannel, String message, Byte mchType, PayChannelMark mark,
                   String payChannelName, Boolean returnPayChargePlatform) {
        this.payChannel = payChannel;
        this.message = message;
        this.mchType = mchType;
        this.mark = mark;
        this.payChannelName = payChannelName;
        this.returnPayChargePlatform = returnPayChargePlatform;
    }

    public static String getName(Integer status, Byte mchType) {
        if (status == null) {
            return ICBC.getMessage();
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (status.equals(statusEnum.getPayChannel()) && mchType.equals(statusEnum.getMchType())) {
                return statusEnum.getMessage();
            }
        }
        return ICBC.getMessage();
    }

    /**
     * 获取支付渠道枚举
     *
     * @param status  支付渠道类型
     * @param mchType 商户模式
     * @return
     */
    public static PayChannelEnum getPayChannel(Integer status, Byte mchType) {
        if (status == null) {
            return DEFAULT_MCH_TYPE;
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (status.equals(statusEnum.getPayChannel()) && mchType.equals(statusEnum.getMchType())) {
                return statusEnum;
            }
        }
        return DEFAULT_MCH_TYPE;
    }

    /**
     * 按服务标识前缀获取商户模式
     *
     * @param mark 支付渠道_商户模式
     * @return
     */
    public static PayChannelEnum getPayChannelByMark(String mark) {
        if (StringUtils.isEmpty(mark)) {
            return DEFAULT_MCH_TYPE;
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (mark.equals(statusEnum.getMark().getMarkPrefix())) {
                return statusEnum;
            }
        }
        return DEFAULT_MCH_TYPE;
    }

    /**
     * 获取支付服务标识
     *
     * @param status
     * @param mchType
     * @return
     */
    public static String getPayMark(Integer status, Byte mchType) {
        if (status == null) {
            return ICBC.getMark().getPayMark();
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (status.equals(statusEnum.getPayChannel()) && mchType.equals(statusEnum.getMchType())) {
                return statusEnum.getMark().getPayMark();
            }
        }
        return ICBC.getMark().getPayMark();
    }

    /**
     * 获取支付渠道名称-不带商户模式
     *
     * @param status
     * @return
     */
    public static String getPayChannelName(Integer status) {
        if (status == null) {
            return ICBC.getPayChannelName();
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (status.equals(statusEnum.getPayChannel())) {
                return statusEnum.getPayChannelName();
            }
        }
        return ICBC.getPayChannelName();
    }

    /**
     * 获取分账服务标识
     *
     * @param status
     * @param mchType
     * @return
     */
    public static String getProftMark(Integer status, Byte mchType) {
        if (status == null) {
            return ICBC.getMark().getProfitMark();
        }
        for (PayChannelEnum statusEnum : PayChannelEnum.values()) {
            if (status.equals(statusEnum.getPayChannel()) && mchType.equals(statusEnum.getMchType())) {
                return statusEnum.getMark().getProfitMark();
            }
        }
        return ICBC.getMark().getProfitMark();
    }

    /**
     * 是微信普通商户
     *
     * @param payChannel
     * @param mchType
     * @return
     */
    public static Boolean isWeChatNormal(Integer payChannel, Byte mchType) {
        return WECHAT_NORMAL.getPayChannel().equals(payChannel) && WECHAT_NORMAL.getMchType().equals(mchType);
    }

    /**
     * 是微信普通服务商
     *
     * @param payChannel
     * @param mchType
     * @return
     */
    public static Boolean isNormalService(Integer payChannel, Byte mchType) {
        return WECHAT_NORMAL_SERVICE.getPayChannel().equals(payChannel) && WECHAT_NORMAL_SERVICE.getMchType().equals(mchType);
    }

    public static Boolean isWeChat(Integer payChannel) {
        return WECHAT_NORMAL.getPayChannel().equals(payChannel);
    }

    public static String getMchTypeName(Byte mchType) {
        final String normal = "普通商户";
        final String service = "服务商";
        final String combine = "服务商（收付通）";
        if (null == mchType) {
            return "";
        }
        if (WECHAT_NORMAL.getMchType().equals(mchType)) {
            return normal;
        }
        if (WECHAT_NORMAL_SERVICE.getMchType().equals(mchType)) {
            return service;
        }
        if (WECHAT_COMBINE.getMchType().equals(mchType)) {
            return combine;
        }
        return normal;
    }


    /**
     * 获得支持进件的商户类型
     * 普通商户支持普通商户进件
     * 普通服务商支持普通服务商、普通商户
     * 电商收付通支持电商收付通、普通商户
     *
     * @param mchType
     * @return
     */
    public static List<Byte> getApplymentMchType(Byte mchType) {

        if (WECHAT_NORMAL.getMchType().equals(mchType)) {
            return Lists.newArrayList(WECHAT_NORMAL.getMchType());
        }
        if (WECHAT_NORMAL_SERVICE.getMchType().equals(mchType)) {
            return Lists.newArrayList(WECHAT_NORMAL.getMchType(), WECHAT_NORMAL_SERVICE.getMchType());
        }
        if (WECHAT_COMBINE.getMchType().equals(mchType)) {
            return Lists.newArrayList(WECHAT_NORMAL.getMchType(), WECHAT_COMBINE.getMchType());
        }
        return Lists.newArrayList();
    }
}
