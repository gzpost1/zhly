package cn.cuiot.dmp.base.application.constant;

/**
 * 微信小程序常量配置
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:15
 */
public interface WxUrlConstant {

    /**
     * 获取用户openId
     */
    String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={code}&grant_type=authorization_code";

    /**
     * 获取accessToken地址
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";

    /**
     * 获取手机号地址
     */
    String USER_PHONENUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token={accessToken}";

}
