package cn.cuiot.dmp.common.constant;

/**
 * @author jiangze
 * @classname RegexConsts
 * @description 正则表达式常量
 * @date 2020-06-23
 */
public class RegexConst {

    /**
     * 手机号，1[3456789]号段
     * 长度：11
     */
    public static final String PHONE_NUMBER_REGEX = "^1[3456789]\\d{9}$";

    /**
     * 巡更点经度校验
     */
    public static final String LONGITUDE_REGEX = "([0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{0,6})";

    /**
     * 巡更点纬度校验
     */
    public static final String LATITUDE_REGEX = "([0-9]|[1-8][0-9]|90)\\.([0-9]{0,6})";

    /**
     * email，字母、数字、下划线、点号、减号组合
     * 长度：5-50
     */
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+{5,50}$";

    /**
     * 用户名，开头必须为英文字母或汉字，支持中文、大小写英文（大小写敏感）、数字、特殊字符（!#@$%^&*.?）
     * 长度：4-20
     */
    public static final String USERNAME_REGEX = "^[A-Za-z\\u4e00-\\u9fa5][a-zA-Z\\u4E00-\\u9FA50-9!#@$%^&*.?_]+$";

    /**
     * 密码，大小写字母、数字、特殊字符（!@#$%^&*.?）混合组成
     * 长度：8-20
     */
    public static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*.?]).{8,20}$";

    /**
     * 角色名称正则校验
     */
    public static final String ROLE_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$";

    /**
     * 角色key正则校验
     */
    public static final String ROLE_KEY = "^[a-zA-Z0-9_]{4,32}$";

    /**
     * 角色备注长度
     */
    public static final int ROLE_DESCRIPTION_LENGTH = 128;

    /**
     * 角色名称长度
     */
    public static final int ROLE_NAME_LENGTH = 32;

    /**
     * 账户名正则
     */
    public static final String ORG_NAME = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,64}$";

    /**
     * 日志服务
     */
    public static final String LOG_REGEX ="^[0-9a-zA-Z\\u4e00-\\u9fa5:!@#$%^&*=.?_/\\s-]+$";

    /**
     * 房屋面积 数字带小数
     */
    public static final String NEW_HOUSE_AREA = "[0-9]+(.[0-9]{1,3})?$";

    /**
     * 企业社会信用代码，只允许大写和数字
     */
    public static final String  SOCIAL_CREDIT_CODE = "^[A-Z0-9]+$";
}
