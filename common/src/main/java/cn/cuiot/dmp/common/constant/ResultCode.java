package cn.cuiot.dmp.common.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * @author jiangze
 * @classname ResultCode
 * @description 返回状态码枚
 * @date 2020-06-24
 */
@Getter
@ToString
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS("000000", "API请求成功"),



    /**
     * 公共错误：系统错误
     */
    INNER_ERROR("010001", "服务器运行内部错误"),

    REQUEST_FORMAT_ERROR("010002", "请求格式不合法"),

    PARAM_CANNOT_NULL("010003", "参数不能为空"),

    INVALID_PARAM_TYPE("010004", "请求参数类型不合法"),

    PARAM_NOT_COMPLIANT("010005", "请求参数不符合规定"),


    OBJECT_NOT_EXIST("011000", "对象不存在"),

    SERVER_BUSY("011002", "服务繁忙，请稍后核对或者重试。"),

    CONTAIN_SENSITIVITY_ERROR("011020","文本包含敏感词："),



    SIGN_ERROR("020008", "签名失败"),

    TOKEN_VERIFICATION_FAILED("021005", "token校验不通过"),
    LOGIN_INVALID("021006", "登录过期"),
    ORG_ID_NOT_EXIST("021007", "orgId不存在"),
    USER_ID_NOT_EXIST("021008", "userId不存在"),
    ROLE_USER_APP_ALREADY_BIND("021010", "该角色已被授予给其他用户或应用，请先解除授予关系"),
    NO_OPERATION_PERMISSION("021011", "无操作权限"),
    ROLE_NOT_EXIST("021012", "角色不存在"),
    DEFAULT_ROLE_NOT_OPERATE("021014", "默认管理员角色不能进行操作"),
    QUERY_ROLE_DETAILS_ERROR("060001", "查询角色详情失败"),
    OPEN_ID_NOT_EXIST("021017", "openId不存在"),
    ROLE_NAME_ALREADY_EXIST("021020", "角色名称已存在，请更换！"),
    PAY_REQUIRED("021021","缴费管理为必选"),
    LABEL_TYPE_NOT_EXIST("021022", "标签类型不存在"),
    ONLY_DEPT_USER_CAN_INSERT_DEPT("021023", "只有组织级用户才可添加组织"),
    ONLY_LEVEL_TWO_DEPT("021024", "组织只可选择市级组织层级，比如江苏省**市，或北京市**区"),
    DEPARTMENT_EXISTS_USER("021026", "该组织下存在用户"),

    /**
     * 账户、用户相关错误
     */
    PHONE_NUMBER_ALREADY_EXIST("050002", "该手机号已注册，请重新输入!"),
    USERNAME_ALREADY_EXIST("050003", "该用户名已注册，请重新输入!"),
    USERNAME_IS_INVALID("050004", "用户名长度为4-20字符"),
    PASSWORD_IS_INVALID("050005", "密码应由大写字母、小写字母、数字和特殊符号（!@#$%^&*.?）混合组成,且不能连续3位以上"),
    PASSWORDS_NOT_EQUALS("050006", "两次密码不一致"),
    EMAIL_IS_INVALID("050007", "邮箱不符合规则"),
    PRIVACY_AGREEMENT_ERROR("050008", "请阅读《用户协议》和《隐私政策》"),

    USER_ACCOUNT_NOT_EXIST("050011", "账号不存在"),
    USER_ACCOUNT_OR_PASSWORD_ERROR("050013", "账号或密码错误"),
    USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR("050014", "账号或密码错误/验证码错误"),
    USER_ACCOUNT_LOCKED_ERROR("050015", "账号被冻结，请30分钟后再登录"),
    KAPTCHA_ERROR("050016", "获取图片验证码失败"),
    ACCESS_ERROR("050017", "访问权限异常"),
    UNAUTHORIZED_ACCESS("050018", "未授权访问"),
    REQ_PARAM_ERROR("050019", "请求参数错误"),
    USER_ACCOUNT_IS_EMPTY("050021", "账号为空"),
    PASSWORD_IS_EMPTY("050022", "密码为空"),
    PHONE_NUMBER_IS_EMPTY("050024", "手机号为空"),
    KAPTCHA_TEXT_IS_EMPTY("050027", "图形验证码为空"),
    KID_IS_EMPTY("050027", "kid为空"),
    SMS_TEXT_IS_EMPTY("050028", "短信验证码为空"),
    RESET_PASSWORD_ERROR("050030", "重置密码失败"),
    UPDATE_PASSWORD_FAIL("050033", "修改密码失败"),
    SMS_COUNT_EXCEEDS_LIMIT("050034", "获取验证码超过单日最大次数"),



    CANNOT_DELETE_ORGOWNER("050036", "不能删除账户所有者"),
    PHONE_NUMBER_EXIST("050039", "手机号已存在"),
    USERNAME_SEARCH_IN_INVALID("050040", "用户名称仅支持中文、大小写字母、数字"),
    SMS_COUNT_EXCEEDS_LIMIT_CUSTOM("050034", "获取验证码每日不得超过%s次"),

    CANNOT_DELETE_SELF("050044", "不能删除自己"),
    KAPTCHA_TEXT_ERROR("050046", "图片验证码错误"),
    SMS_CODE_ERROR("050047", "短信验证码错误"),
    PHONE_NUMBER_IS_INVALID("050048", "手机号无效"),
    PHONE_NUMBER_IS_NOT_VALID("050049", "手机号不符合规则"),
    KAPTCHA_EXPIRED_ERROR("050050", "图片验证码过期"),
    SMS_CODE_EXPIRED_ERROR("050051", "验证码已过期，请重新获取"),
    SMS_CODE_FREQUENTLY_REQ_ERROR("050052", "短时间内请勿重复提交"),
    ORG_NAME_ERROR("050053", "账户名称不规范，应可控制在32个字符以内,内容应该包括数字、字母、中文"),
    USER_USERNAME_ERRER("050056", "用户名称重复，请修改后重试"),
    ORG_ORGKEY_ERRER("050057", "账户key重复，请修改后重试"),
    ROLE_ROLEKEY_ERROR("050058", "角色key不存在"),
    CANNOT_OPERATION("050059", "不可操作"),
    CANNOT_SWITCH("050065", "不能更改账户所有者角色"),
    ORG_IS_ABNORMAL("050066", "账户异常"),
    ORG_IS_NOT_EXIST("050067", "账户不存在"),
    USERNAME_IS_SENSITIVE_WORD("050080", "用户名包含了非法字符，请重新输入！"),

    OLD_PASSWORD_IS_ERROR("050081", "您的原密码输入有误!"),
    USERNAME_CANNOT_UPDATE("050088", "用户名不允许修改！"),
    ROLE_NAME_FORMAT_ERROR("050091", "角色名称输入不符合规范"),
    SMS_TEXT_OLD_INVALID("050093", "旧手机短信验证错误,可能您更换手机号操作超时,请重新操作！"),
    SMS_TEXT_ERROR("050094", "验证码错误, 请稍后重新获取!"),
    USER_DEFAULT_ROLE_NOT_ALLOW_ERROR("050095", "管理角色只有一个，不允许创建多个！"),
    ORG_IS_ENABLED("050096", "账户已禁用"),

    ACCOUNT_HAS_COMMUNITY("050111", "账户下存在小区"),
    ACCOUNT_HAS_USER("050112", "账户下存在用户"),
    ACCOUNT_DELETED_ERROR("050113", "账户删除失败"),
    SOCIAL_CREDIT_CODE_EXIST("050118", "统一社会信用代码已存在"),
    COMPANY_INFO_ALREADY_EXIST("050119", "公司信息已填写，无法修改"),

    KID_EXPIRED_ERROR("050122", "kid已过期，请重新获取"),



    DEPT_NOT_EXISTS("060007", "组织不存在"),
    QUERY_USER_DEPT_ERROR("060007", "查询用户组织信息异常"),


    /**
     * 调用第三方服务出错->中间件服务出错
     */
    SEND_SMS_ERROR("060002", "短信发送错误，请稍后重试"),

    /**
     * 其他异常
     */
    CANNOT_CREATE_CONST_CLASS_OBJECT("080011", "无法创建常量类对象"),


    /**
     * 菜单相关
     */
    COMPONENT_URI_IS_EXIST("140001", "菜单组件已存在"),
    MENU_NAME_IS_EXIST("140002", "菜单名称已存在"),
    MENU_DESCRIPTION_IS_EXIST("140003", "菜单描述已存在"),
    PERMISSION_CODE_NOT_NULL("140004", "按钮权限代码不能为空"),
    PARENT_MENU_NOT_EXIST("140005", "父级菜单不存在"),
    ADD_MENU_FAIL("140006", "添加菜单失败"),
    PARAM_NOT_NULL("140007", "参数不能为空"),





    /**
     * 组织相关
     */
    DEPARTMENT_NAME_IS_EXIST("150001", "同级别下组织名称已存在"),
    DEPARTMENT_HAS_CHILDREN("150002", "该组织存在下级组织,不可删除！"),
    DEPARTMENT_ROOT_NO_DELETE("150005", "根组织不可删除"),
    DEPARTMENT_HAS_USER("150006", "组织内已有人员,不可删除！"),
    DEPARTMENT_HAS_ORG("150007", "已存在关联账户,不可删除"),
    DEPARTMENT_ULTRA_VIRES("150008", "所选组织没有操作权限"),
    COMMUNITY_NAME_IS_EXIST("150009", "小区名称已存在"),
    DEPARTMENT_LEVEL_OVERRUN("150011", "组织最大层级超过限制"),
    DEPARTMENT_IS_INIT("150012", "初始化数据,不可删除"),
    BUILDING_HAS_CHILDREN("150013", "楼栋已存在房屋数据,不可删除,请先删除房屋"),
    DEPARTMENT_QUERY_VIRES("1500014", "所选组织没有查看权限"),



    BUILDING_NAME_IS_EXIST("1500015", "该小区下已存在该楼栋"),
    DUPLICATE_REQUEST("150021", "请勿重复提交！"),

    DEPARTMENT_NAME_EXIST("150024","组织名称已存在"),

    BUILDING_NAME_NOT_EXIST("150029", "楼座名称不存在"),

    FLOOR_NAME_IS_EXIST("1500034", "该楼座下已存在该楼层"),
    FLOOR_HAS_CHILDREN("150035", "楼层下已存在数据,不可删除,请先删除房屋或商铺"),

    REGION_NAME_IS_EXIST("150035", "该园区下已存在该区域"),
    REGION_BUILDING_NAME_IS_EXIST("150036", "该区域下已存在该楼栋"),
    PARK_NAME_IS_EXIST("150037","该园区已存在"),
    REGION_HAS_CHILDREN("150036", "区域已存在楼栋数据,不可删除,请先删除楼栋"),
    CURRENT_FLOOR_IS_NUMBER("1500039", "当前楼层只支持数字"),
    PARK_HAS_CHILDREN("1500043", "园区已存在下级数据,不可删除,请先删除下级数据"),
    COMMERCIAL_BUILDING_HAS_CHILDREN("1500044", "楼栋已存在楼层数据,不可删除,请先删除楼层"),
    CURRENT_FLOOR_NOT_ZERO("1500046", "当前楼层不能是0"),
    CURRENT_FLOOR_MORE_THEN_FLOOR("1500047", "当前楼层高度不能超过楼座层数，不能低于-4层"),
    CURRENT_FLOOR_NOT_UPDATE("150054", "楼层已存在房屋或商铺数据,当前楼层数不可修改"),
    SPACE_USER_ALREADY_USE("1500057", "当前场所存在用户，无法删除"),


    DEPARTMENT_NOT_HAS_CHILDREN("150102", "不存在下级组织"),
    HOUSE_HAS_EXIST("170002", "该房屋已存在"),
    REGION_NOT_NULL("170011", "房屋所属区域不得为空"),
    HOUSE_USE_AREA_SUPPORT_NUM("170012","房屋使用面积仅支持正数，最大支持三位小数"),
    HOUSE_PUBLIC_AREA_SUPPORT_NUM("170013","房屋公摊面积仅支持正数，最大支持三位小数"),


    /**
     * 标签相关
     */
    OTHER_LABEL_NAME_NOT_NULL("7000001","其它商企标签名称为空"),
    ACCOUNT_LABEL_NOT_EXIST("7000002","所选账户标签不存在"),

    /**
     * 系统服务相关（前两位表示服务模块，中间三位表示所属业务，后两位表示异常编码）
     */
    QUERY_BUSINESS_TYPE_ERROR("0100101", "查询业务类型信息异常"),
    ;





    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 私有构造函数
     *
     * @param code    状态码
     * @param message 状态信息
     * @return
     */
    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
