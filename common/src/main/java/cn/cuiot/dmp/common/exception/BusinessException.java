package cn.cuiot.dmp.common.exception;

import cn.cuiot.dmp.common.constant.ResultCode;
import org.apache.commons.lang3.StringUtils;

/**
* @描述:  发生业务异常时抛出此异常
*
* @Author:  sunking
* @Date:    2020/7/20 3:10 PM
*/
public class BusinessException extends RuntimeException {

    protected String exceptionMessage;
    protected String errorCode;

    public BusinessException() {
        super();
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.errorCode = resultCode.getCode();
        this.exceptionMessage = resultCode.getMessage();
    }

    public BusinessException(ResultCode resultCode,Object... params) {
        super(resultCode.getMessage());
        this.errorCode = resultCode.getCode();
        this.exceptionMessage = String.format(resultCode.getMessage(),params);
    }

    public BusinessException(ResultCode resultCode, String exceptionMessage) {
        super(resultCode.getMessage());
        if(StringUtils.isNotBlank(exceptionMessage)){
            this.exceptionMessage = exceptionMessage;
        }
        this.errorCode = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.errorCode = resultCode.getCode();
        this.exceptionMessage = resultCode.getMessage();
    }

    public BusinessException(String resultCode, String exceptionMessage) {
        super(exceptionMessage);
        if(StringUtils.isNotBlank(exceptionMessage)){
            this.exceptionMessage = exceptionMessage;
        }
        this.errorCode = resultCode;
    }

    /**
     * 获取异常编码
     *
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取异常消息
     *
     * @return
     */
    public String getErrorMessage() {

        return exceptionMessage;
    }

}
