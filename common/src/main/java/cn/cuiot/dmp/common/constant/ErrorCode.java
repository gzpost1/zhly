package cn.cuiot.dmp.common.constant;

/**
 * Created by yingcan on 2019/10/22.
 */
public enum ErrorCode implements IExceptionType {
    /* 系统以SYS开头，错误码为-00xxx ，
      联通 错误码为10xxx,
      腾讯 错误码为20XXX*,
    */
    UNKNOWN("-1", "未知错误"),
    SUCCESS("00000", "成功"),
    COMMON_FAILURE("00002", "通用错误"),
    BUSINESS_EXCEPTION("00003", "业务异常"),
    SQL_ERROR("00004", "SQL错误"),
    CONTENT_TYPE_NOT_SUPPORTED("00005", "类型不支持"),
    NOT_FOUND("00006", "未找到"),
    DATA_EXIST("00018", "信息已存在"),
    AREA_NOT_EXIST("00007", "区域不存在"),
    PHONE_FORMAT_ERROR("00008", "手机格式错误"),
    EXCEL_EXPORT_ERROR("00009", "excel导出异常"),
    PROBE_FAILURE("00019", "failed to call probe interface"),
    DICT_VALUE_EXISTED("00023", "该字典键值已存在"),
    EXCEL_IMPORT_ERROR("00011", "excel导入异常"),
    // 请求
    PARAM_MISS("00020", "缺失参数"),
    PARAM_INVALID("00021", "输入参数的格式不正确"),
    PARAM_WRONG("00022", "参数错误"),
    REQUEST_THIRDPARTY_FAILURE("00007", "request other system failure"),

    // 登录
    ACCOUNT_WRONG("00030", "账户或密码错误"),
    ACCOUNT_OR_PASSWORD_ERROR_EXCEED_THREE("000031", "账户或密码错误错误次数超过3次，请输入验证码"),
    GET_CAPTCHA_ERROR("00032", "获取验证码错误"),
    INPUT_CAPTCHA_ERROR("00033", "输入验证码错误"),
    PASSWORD_ENCRYPT_ERROR("00034", "加密错误，请联系管理员"),
    ACCOUNT_OR_PASSWORD_ERROR_EXCEED_THREE_WITH_CAPTCHA("00035", "输入验证码情况下的账户或密码错误"),
    DECRYPT_ERROR("00036", "解密错误，请联系管理员"),
    MINI_LOGIN_ERROR("00037", "小程序登录错误，请联系管理员"),
    PHONE_GET_FAIL("00038", "获取手机号错误"),
    UPDATE_PASSWORD_FAIL("00039", "原始密码错误"),
    USER_DISABLE("00040", "用户不可用"),
    GET_USER_INFO_ERROR("00041", "获取用户账户异常"),
    CAPTCHA_EXPIRE("00033", "验证码过期"),
    OFFLINE("00042", "该账号已在其他设备登录"), //踢人下线，只能用于单一登录

    // token
    USER_TOKEN_EXPIRED("00050", "用户凭证过期，请重新登录"),
    USER_TOKEN_SIGNATURE_ERROR("00051", "用户凭证签名错误，请联系管理员"),
    USER_TOKEN_EMPTY("00052", "用户凭证缺失"),
    JWT_RESOLVE_ERROR("00053", "用户凭证解析失败，请联系管理员"),

    // 上传
    UPLOAD_FAILURE("00060", "上传文件失败"),
    UPLOAD_NOT_SUPPORTED("00061", "文件类型不支持"),
    UPLOAD_SIZE_EXCEEDED("00062", "文件过大"),

    // 用户
    ACCOUNT_DISABLED("00070", "账号已禁用，请联系管理员"),
    ACCOUNT_HAVE_EXISTS("00071", "账号已存在"),
    USER_HAVE_BIND("00072", "已绑定了平台"),
    USER_NOT_BIND("00073", "没有绑定平台"),
    ROLE_NOT_AVAILABLE("00074", "角色不可用"),
    USER_NOT_EXISTS("00075", "用户不存在"),
    USER_NOT_BIND_PLATFORM("00076", "当前用户还没有绑定任何平台"),
    MUST_BE_AREA_USER("00077", "必须是区县用户"),
    WEAK_PASSWORD("00078", "密码需在8到30位，由字母、数字和特殊字符组成，至少包含大写字母、小写字母和数字。"),


    // 角色
    NO_ROLE("00090", "账号未分配角色"),
    ROLE_NOT_EXISTS("00091", "角色不存在"),
    ROLE_HAVE_EXISTS("00092", "角色已存在"),

    // 平台
    NO_PERMISSION("00100", "无权限"),
    PLATFORM_AUDIT_MUST_AWAIT_STATUS("00101", "审核前必须为等待状态"),
    PLATFORM_HAVE_ADMIN("00102", "平台已有管理员"),
    PLATFORM_NOT_EXIST("00103", "平台不存在"),
    PLATFORM_HAVE_EXIST("00104", "当前区域已有平台"),
    PLATFORM_NOT_AVAILABLE("00105", "平台不可用"),
    PLATFORM_NOT_BIND_USER("00106", "平台未绑定用户"),
    AUDITOR_MUST_BE_PLATFORM_USER("00107", "审核人必须是平台用户"),
    NOT_SELECT_PLATFORM("00108", "请选择平台"),
    NOT_AUDIT_PERMISSION("00109", "无审核权限"),
    DEPT_DISABLED("00110", "部门停用"),
    DICT_HAS_USED("00110", "字典已分配"),
    POST_HAS_USED("00110", "岗位已分配"),
    ROLE_HAS_USED("00110", "角色已分配"),

    // 删除
    DELETE_FAILURE("00109", "删除错误"),
    DELETE_FAILURE_ORGANIZATION_HAVE_USER("001010", "存在用户，不能删除"),
    DELETE_FAILURE_ORGANIZATION_HAVE_SON("00111", "存在子组织，不能删除"),
    DELETE_ADMIN_FAILURE("00112", "管理员不能删除"),
    DELETE_ROOT_FAILURE("00113", "根节点不能删除"),
    DELETE_FAILURE_ROLE_HAVE_USER("00114", "有用户正在使用此角色，不能删除"),

    // 组织
    COMPANY_NOT_EXISTS("00120", "公司不存在"),
    DEPARTMENT_HAVE_EXISTS("00121", "部门已存在"),
    DEPARTMENT_NOT_EXISTS("00122", "部门不存在"),
    ROOT_DEPARTMENT_NOT_ALLOW_ADD_USER("00123", "根组织不能添加用户"),

    INIT_SYSTEM_PARAMS_ERROR("00123", "初始化系统配置参数失败"),

    // 腾讯
    ERROR_API_NO_APPID("20001", "apiId相关信息不存在,请联系我方"),
    ERROR_API_NO_BODY("20002", "请求body不能为空"),
    ERROR_API_TOKEN_ALREADY_USE("20003", "token已被别人使用,请重新登录获取token"),
    ERROR_API_VALISIGN_FALID("20004", "api验签失败"),
    ERROR_API_VALIAUTH_ADCODE_FAILD("20005", "apiId没有查询该区域的权限"),
    ERROR_API_NO_ADCODE_PARAM("20006", "api请求参数没有携带区域adcode参数,请携带参数"),
    TOKEN_NULL("20007", "token is null"),
    ERROR_API_GET_TOKEN_FAILD("20008", "获取token失败,请检查appid是否正确"),
    ERROR_API_REDIS_GET_TOKEN_FAILD("20009", "token获取失败,请检查是否正确获取token"),


    //环境监测
    ERROR_DEVICE_NOT_EXIST("20002", "请求body不能为空"),

    REQUEST_HYDROLOGICAL_SYSTEM_FAILURE("20000", "request other hydrological system failure"),
    REQUEST_BROADCAST_SYSTEM_FAILURE("13000", "request other broadcast system failure"),

    //事件
    EVENT_NOT_EXIST("40001", "事件不存在"),

    //音视频会议
    /**
     * 会议室已存在
     */
    EXISTENCE("000002", "会议室已存在"),
    /**
     * 参会人员已满
     */
    LIMIT_EXCEEDED("000003", "参会人员已满"),
    /**
     * 视频流人数已满
     */
    LIMIT_VIDEO("000004", "视频流人数已满或全员禁画"),
    /**
     * 音频流人数已满
     */
    LIMIT_AUDIO("000004", "音频流人数已满或全员禁音"),

    /**
     * 名称重复
     */
    DUPLICATE_NAME("000005", "名称重复"),

    /**
     * 钉钉 企业内部应用 业务异常
     */
    ERROR_DINGTALK_BUSINESS("70001", "钉钉业务异常"),

    ;

    private String code;
    private String msg;

    private ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
