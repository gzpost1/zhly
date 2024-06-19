package cn.cuiot.dmp.common.constant;

/**
 * @author jiangze
 * @classname SecurityConst
 * @description 安全相关常量
 * @date 2020-07-14
 */
public class SecurityConst {

    /**
     * 图形验证码过期时间，单位（秒） 5分钟
     */
    public static final int KAPTCHA_EXPIRED_TIME = 5 * 60;

    /**
     * SESSION时间，单位（秒） 12小时
     */
    public static final int REFRESH_SESSION_TIME =12 * 60 * 60;

    /**
     * APP SESSION时间，单位（秒） 15天
     */
    public static final int WX_REFRESH_SESSION_TIME =15 * 24 * 60 * 60;

    /**
     * 短信验证码调用间隔时间，单位（分钟） 3分钟
     */
    public static final int SMS_CODE_FORBIDDEN_TIME = 3;

    /**
     * 短信验证码过期时间，单位（分钟） 3分钟
     */
    public static final int SMS_CODE_EXPIRED_TIME = 3;

    /**
     * 登录失败冻结时间，单位（分钟） 30分钟
     *
     */
    public static final long LOGIN_FAILED_FREEZE_TIME = 30;

    /**
     * 最大失败登录次数
     */
    public static final long LOGIN_FAILED_MAX_COUNTS = 5;

    /**
     * 用户登录的账户历史记录个数
     */
    public static final int USER_ORG_LOGIN_RECORD_MAX = 5;

}
