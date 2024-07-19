package cn.cuiot.dmp.common.constant;

/**
 * Created by yingcan on 2019/10/22.
 */
public enum ErrorCode implements IExceptionType {

    UNKNOWN("-1", "未知错误"),
    SUCCESS("00000", "成功"),
    COMMON_FAILURE("00002", "通用错误"),
    BUSINESS_EXCEPTION("00003", "业务异常"),
    SQL_ERROR("00004", "SQL错误"),
    CONTENT_TYPE_NOT_SUPPORTED("00005", "类型不支持"),
    NOT_FOUND("00006", "未找到"),

    NOT_OPERATION("00005","无权操作"),
    SUSPENDED("00007","任务已挂起"),

    TASK_COMPLETE("00008","任务已结束"),

    TASK_ALREADY_EXISTS("00009","任务已存在")
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
