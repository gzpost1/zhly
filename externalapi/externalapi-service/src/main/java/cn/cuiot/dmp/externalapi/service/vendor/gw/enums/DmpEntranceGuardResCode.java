package cn.cuiot.dmp.externalapi.service.vendor.gw.enums;


import cn.cuiot.dmp.common.constant.ErrorCode;

import java.util.Objects;

/**
 * 格物门禁-响应编码枚举
 *
 * @Author: zc
 * @Date: 2024-02-27
 */
public enum DmpEntranceGuardResCode {
    /**
     * 成功
     */
    SUCCESS_000000("000000", ErrorCode.SUCCESS.code(), "成功"),
    /**
     * 未知错误
     */
    ERROR_010001("010001", ErrorCode.UNKNOWN.code(), "服务器运行内部错误"),
    /**
     * 参数不能为空
     */
    ERROR_010003("010003", ErrorCode.UNKNOWN.code(), "参数不能为空"),
    /**
     * 请求参数不合法
     */
    ERROR_010004("010004", ErrorCode.UNKNOWN.code(), "请求参数不合法"),
    /**
     * 每页查询数量超出最大值
     */
    ERROR_010007("010007", ErrorCode.UNKNOWN.code(), "每页查询数量超出最大值"),
    /**
     * app_id不存在
     */
    ERROR_021001("021001", ErrorCode.UNKNOWN.code(), "app_id不存在"),
    /**
     * 该app_id未授权访问该API
     */
    ERROR_021002("021002", ErrorCode.UNKNOWN.code(), "该app_id未授权访问该API"),
    /**
     * 指定的时间戳或日期值已过期
     */
    ERROR_021003("021003", ErrorCode.UNKNOWN.code(), "指定的时间戳或日期值已过期"),
    /**
     * 请求中trans_id重复
     */
    ERROR_021004("021004", ErrorCode.UNKNOWN.code(), "请求中trans_id重复"),
    /**
     * token校验未通过
     */
    ERROR_021005("021005", ErrorCode.UNKNOWN.code(), "token校验未通过"),
    /**
     * 应用已被禁用
     */
    ERROR_021006("021006", ErrorCode.UNKNOWN.code(), "应用已被禁用"),
    /**
     * 未知异常
     */
    ERROR_UNKNOWN("-1", ErrorCode.UNKNOWN.code(), "未知异常"),
    ;
    private String code;
    private String errorCode;
    private String msg;

    private DmpEntranceGuardResCode(String code, String errorCode, String msg) {
        this.code = code;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static DmpEntranceGuardResCode getItemByCode(String code) {
        if (Objects.nonNull(code)) {
            DmpEntranceGuardResCode[] enumConstants = DmpEntranceGuardResCode.class.getEnumConstants();
            for (DmpEntranceGuardResCode item : enumConstants) {
                if (code.equals(item.getCode())) {
                    return item;
                }
            }
        }
        return ERROR_UNKNOWN;
    }
}
