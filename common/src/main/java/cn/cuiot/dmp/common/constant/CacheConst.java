package cn.cuiot.dmp.common.constant;

/**
 * @author jiangze
 * @classname RedisKeyConst
 * @description 缓存相关常量
 * @date 2020-07-14
 */
public class CacheConst {

    /**
     * 图形验证码 redisKey
     */
    public static final String KAPTCHA_TEXT_REDIS_KEY = "KAPTCHA:TEXT:REDIS:KEY:";

    /**
     * 登录用户jwt
     */
    public static final String LOGIN_USERS_JWT = "LOGIN:USERS:JWT:";

    /**
     * 用户长期登录
     */
    public static final String USER_LONG_TIME_LOGIN = "USER:LONG:TIME:LOGIN";

    /**
     * 账户禁用，踢下线
     */
    public static final String LOGIN_ORG_TO_KICK = "LOGIN:ORG:KICK:";

    /**
     * 微信登录用户jwt
     */
    public static final String LOGIN_USERS_JWT_WX = "LOGIN:USERS:JWT:WX:";

    /**
     * 登录用户refresh code
     */
    public static final String LOGIN_USERS_REFRESH_CODE = "LOGIN:USERS:REFRESH:CODE:";

    /**
     * 邮箱验证码 (忘记密码)redisKey
     */
    public static final String EMAIL_CODE_TEXT_REDIS_KEY_FOR_RESET_PASSWORD = "EMAIL_CODE_TEXT:RESET_PASSWORD:";

    /**
     * 短信验证码 redisKey
     */
    public static final String SMS_CODE_TEXT_REDIS_KEY = "SMS_CODE_TEXT_REDIS_KEY";

    /**
     * 登录时使用的secretInfo
     */
    public static final String SECRET_INFO_KEY = "SECRET_INFO_KEY";

    /**
     * 短信验证码 (修改手机号)redisKey
     */
    public static final String SMS_CODE_TEXT_REDIS_KEY_P = "SMS_CODE_TEXT_REDIS_KEY_P";

    /**
     * 1分钟内是否已发送过短信验证码 redisKey
     */
    public static final String SMS_ALREADY_SEND_REDIS_KEY = "SMS_ALREADY_SEND_REDIS_KEY";

    /**
     * 1分钟内是否已发送过短信验证码（修改手机号） redisKey
     */
    public static final String SMS_ALREADY_SEND_REDIS_KEY_P = "SMS_ALREADY_SEND_REDIS_KEY_P";

    /**
     * 系统中存在的用户
     * 登录失败用户，使用userId作为redisKey
     */
    public static final String LOGIN_FAILED_USERS_REDIS_KEY = "LOGIN_FAILED_USERS_REDIS_KEY";

    /**
     * 系统中不存在的用户
     * 登录失败用户，使用登录名作为redisKey
     */
    public static final String LOGIN_FAILED_NON_EXIST_USERS_REDIS_KEY = "LOGIN_FAILED_NON_EXIST_USERS_REDIS_KEY";

    /**
     * 用户最近登录账户(至多保留5个)
     */
    public static final String USER_ORG_CURRENT_KEY = "USER:ORG:CURRENT:KEY";

    /**
     * 组织管理 redisKey
     */
    public static final String ROBOT_ORGANIZATION_REDIS_KEY = "ORG:DEPARTMENT:ORGANIZATION";

    /**
     * 组织管理 redisKey
     */
    public static final String ORGANIZATION_INSERT_REDIS_KEY = "ORG:DEPARTMENT:ORGANIZATION:INSERT";

    /**
     * AREA表 查询行政区域编码 redisKey
     */
    public static final String AREA_CODE_CONFIG = "AREA:AREA_CODE:";

    public static final String USER_CACHE_KEY_PREFIX = "saas:community:user:";

    /**
     * key：组织id
     * value：行政区划编码
     */
    public static final String DEPT_CODE_KEY_PREFIX = "dept:id:code:";

    /**
     * key：组织id
     * value：组织名称
     */
    public static final String DEPT_NAME_KEY_PREFIX = "dept:id:name:";

    /**
     * 登录获取验证码一个手机号一天
     */
    public static final String LOGIN_SMS_CODE_ONE_DAY_PHONE_NUMBER = "LOGIN:SMS:CODE:ONEDAY:PHONENUMBER:";

    /**
     * 手机验证码一天发送的最大次数
     */
    public static final String SMS_CODE_MAX_COUNT = "SMS_CODE_MAX_COUNT:";

    /**
     * 签名验证重放
     */
    public static final String SIGNATURE_NONCE_KEY = "SIGNATURE:NONCE_KEY:";

}
