package cn.cuiot.dmp.sms.enums;


import java.util.Objects;

/**
 * 云智眼-响应编码枚举
 *
 * @Author: zc
 * @Date: 2024-02-27
 */
public enum SmsHttpRespCode {

    SUCCESS(0, "成功"),
    FAILURE(9, "失败"),
    PROCESS_TIMEOUT(10, "处理超时"),
    TOO_MANY_REQUESTS(429, "请求过多"),
    INSUFFICIENT_BALANCE(999, "余额不足"),
    ACCOUNT_PASSWORD_ERROR(1000, "账号/密码错误"),
    PHONE_NUMBER_ERROR(1001, "手机号码错误"),
    CONTENT_FORMAT_ERROR(1002, "内容格式错误"),
    TEMPLATE_ID_ERROR(1003, "模板id错误"),
    SCHEDULE_TIME_FORMAT_ERROR(1004, "定时时间格式错误"),
    CUSTOM_ID_LENGTH_EXCEEDED(1005, "自定义id不可超过36位"),
    MAXIMUM_NUMBER_LIMIT_EXCEEDED(1006, "号码数已达上限"),
    MINIMUM_SCHEDULE_TIME_EXCEEDED(1007, "定时必须大于10分钟"),
    UNSUPPORTED_PACKAGE(1008, "未支持的套餐"),
    ACCOUNT_NOT_ENABLED(1009, "账户未启用"),
    IP_VERIFICATION_FAILED(1010, "IP校验未通过"),
    OUT_OF_SERVICE_TIME_RANGE(1011, "未在服务时间范围"),
    CONTENT_CHARACTER_LIMIT_EXCEEDED(1012, "内容字数已达上限"),
    DEFAULT_PARAMETERS_NOT_CONFIGURED(5000, "缺省参数未配置"),
    OTHER_ERROR(5001, "其他错误"),
    FREQUENCY_TOO_FAST(5002, "调用频率过快，接口调用时间间隔"),
    FREQUENCY_LIMIT_PER_SECOND(5003, "调用频次过快，接口每秒调用次数上限"),
    TIMESTAMP_EXPIRED(5004, "时间戳已过期"),
    EXTENSION_NUMBER_ERROR(10000, "扩展号错误"),
    SMS_SIGNATURE_ERROR(10001, "短信签名错误"),
    SMS_TEMPLATE_MISMATCH(10002, "短信模板不匹配"),
    SMS_KEY_ACCOUNT_NOT_EXIST(10003, "短信密钥账号不存在"),
    SMS_BUSINESS_NOT_ACTIVATED(10004, "未激活短信业务"),
    CONTENT_CONTAINS_KEYWORD(10005, "内容存在关键字"),
    EXCEL_SMS_FORMAT_ERROR(10006, "Excel短信格式错误，过滤后无数据"),
    SMS_CONTENT_CONTAINS_SIGNATURE(10007, "短信内容中存在签名"),
    SMS_TEMPLATE_NOT_OPEN(10008, "未开通短信模板"),
    CONNECTION_MODE_ERROR(10009, "连接方式错误"),
    CURRENT_OPERATION_LOCKED(10010, "当前操作已被锁定"),
    TEMPLATE_NUMBER_LIMIT_EXCEEDED(10011, "模板数量已达上限"),
    DUPLICATE_TEMPLATE(10012, "存在相同的模板"),
    DUPLICATE_SIGNATURE(10013, "存在相同的签名"),
    SIGNATURE_NUMBER_LIMIT_EXCEEDED(10014, "签名数量已达上限"),
    SMS_KEY_LOCKED(10015, "短信密钥已被锁定"),
    VOICE_KEY_ACCOUNT_NOT_EXIST(15000, "语音密钥账号不存在"),
    VOICE_BUSINESS_NOT_ACTIVATED(15001, "未激活语音业务"),
    VOICE_TEMPLATE_MISMATCH(15002, "语音模板不匹配"),
    VOICE_TEMPLATE_CONFIGURATION_ERROR(15003, "语音模板配置错误"),
    VOICE_EXCEL_FORMAT_ERROR(15004, "语音Excel信息格式错误"),
    VOICE_VARIABLE_FORMAT_ERROR(15005, "语音模板变量格式错误"),
    UNKNOWN(-1, "未知错误码");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    SmsHttpRespCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static SmsHttpRespCode getItemByCode(Integer code) {
        if (Objects.nonNull(code)) {
            SmsHttpRespCode[] enumConstants = SmsHttpRespCode.class.getEnumConstants();
            for (SmsHttpRespCode item : enumConstants) {
                if (code.equals(item.getCode())) {
                    return item;
                }
            }
        }
        return UNKNOWN;
    }
}
