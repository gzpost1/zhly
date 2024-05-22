package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;

/**
 * @author zhangxp207
 */
public class Const {

    private Const() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }
    /**
     * 密钥
     */
    public static final String SECRET = "-3h2b9eEr+(Q_#z+2L@&";

    /**
     * SESSION时间，单位（秒）
     */
    public static final int SESSION_TIME = 4 * 60 * 60;

    /**
     * 用户长期登录SESSION时间，单位（秒）
     */
    public static final int USER_LONG_TIME_LOGIN_SESSION_TIME = 3 * 24 * 60 * 60;

    /**
     * 微信小程序SESSION时间，单位（秒）7天
     */
    public static final long WX_SESSION_TIME = 7 * 24 * 60 * 60;

    /**
     * 一天的秒数
     */
    public static final int ONE_DAY_SECOND = 60 * 60 * 24;

    /**
     * 数字0
     */
    public static final int NUMBER_0 = 0;

    /**
     * 数字1
     */
    public static final int NUMBER_1 = 1;

    /**
     * 数字2
     */
    public static final int NUMBER_2 = 2;
    /**
     * 数字3
     */
    public static final int NUMBER_3 = 3;
    /**
     * 数字4
     */
    public static final int NUMBER_4 = 4;
    /**
     * 数字5
     */
    public static final int NUMBER_5 = 5;
    /**
     * 数字6
     */
    public static final int NUMBER_6 = 6;
    /**
     * 数字7
     */
    public static final int NUMBER_7 = 7;
    /**
     * 数字8
     */
    public static final int NUMBER_8 = 8;
    /**
     * 数字9
     */
    public static final int NUMBER_9 = 9;

    /**
     * 数字11
     */
    public static final int NUMBER_11 = 11;

    /**
     * 数字20
     */
    public static final int NUMBER_20 = 20;

    /**
     * 数字25
     */
    public static final int NUMBER_25 = 25;

    /**
     * 数字60
     */
    public static final int NUMBER_60 = 60;

    /**
     * 数字60
     */
    public static final int NUMBER_100 = 100;

    /**
     * 数字30
     */
    public static final int NUMBER_30 = 30;

    /**
     * 字符串1
     */
    public static final String STR_1 = "1";
    /**
     * 字符串2
     */
    public static final String STR_2 = "2";

    /**
     * 字符串3
     */
    public static final String STR_3 = "3";

    /**
     * 字符串10
     */
    public static final String STR_TEN = "10";

    /**
     * 账户名称长度常量
     */
    public static final Integer ORG_NAME_LENGHT = 32;

    /**
     * 字符串0
     */
    public static final String STR_0 = "0";


}
