package cn.cuiot.dmp.sms.contant;

/**
 * 缓存key
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
public class SmsRedisKeyConstant {

    /**
     * 拼接模版id
     */
    public static final String TEMPLATE = "SMS:TEMPLATE:";

    /**
     * 拼接签名id（平台）
     */
    public static final String SIGN_PLATFORM = "SMS:SIGN:PLATFORM";

    /**
     * 拼接签名id（企业）
     */
    public static final String SIGN_COMPANY = "SMS:SIGN:COMPANY";
}
