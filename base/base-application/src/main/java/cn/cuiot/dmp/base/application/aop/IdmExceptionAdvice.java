package cn.cuiot.dmp.base.application.aop;

import cn.cuiot.dmp.base.application.mica.xss.exception.XssException;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author zjb
 * @classname IdmExceptionAdvice
 * @description 统一处理异常
 * @date 2021-11-18
 */
@Order(1)
@Slf4j
@RestControllerAdvice
public class IdmExceptionAdvice {


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object defaultErrorHandler(HttpRequestMethodNotSupportedException exception) {
        log.error("", exception);
        exception.printStackTrace();
        return new IdmResDTO<>(ResultCode.METHOD_NOT_SUPPORTED);
    }

    /**
     * 统一处理异常的默认方法（应按业务需求编写不同类型的异常处理器）
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("", e);
        e.printStackTrace();
        String code = ResultCode.SERVER_BUSY.getCode();
        String message = ResultCode.SERVER_BUSY.getMessage();
        if (e instanceof HttpMediaTypeNotSupportedException) {
            code = ResultCode.CONTENT_TYPE_NOT_SUPPORTED.getCode();
            message = ResultCode.CONTENT_TYPE_NOT_SUPPORTED.getCode();
        }
        if (e instanceof SQLException || e instanceof DataAccessException) {
            message = e.getMessage().contains("too long") ? "存在字段长度过长，请检查后重新提交" :"执行SQL错误";
        }
        if(e instanceof XssException){
            code = ResultCode.INVALID_PARAM_TYPE.getCode();
            message = e.getMessage();
        }
        return new IdmResDTO<>(code,message);
    }

    /**
     * function: NullPointerException
     *
     * @auther sunking
     * @date 2020/7/30 3:11 下午
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Object defaultErrorHandler(NullPointerException exception) {
        log.error("", exception);
        exception.printStackTrace();
        return new IdmResDTO<>(ResultCode.OBJECT_NOT_EXIST);
    }

    /**
     * function: TypeMismatchException异常处理
     *
     * @auther sunking
     * @date 2020/7/30 3:11 下午
     */
    @ExceptionHandler(value = TypeMismatchException.class)
    public Object defaultErrorHandler(TypeMismatchException exception) {
        log.error("", exception);
        exception.printStackTrace();
        return new IdmResDTO<>(ResultCode.PARAM_NOT_COMPLIANT);
    }

    /**
     * function: ServletRequestBindingException异常处理
     *
     * @date 2020/7/30 3:11 下午
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public Object defaultErrorHandler(ServletRequestBindingException exception) {
        log.error("", exception);
        return new IdmResDTO<>(ResultCode.PARAM_CANNOT_NULL);
    }

    /**
     * function: MethodArgumentNotValidException异常处理
     *
     * @auther 参数校验全局异常处理
     * @date 2020/7/30 3:12 下午
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object defaultErrorHandler(MethodArgumentNotValidException exception) {
        StringBuilder errorMsg = new StringBuilder();
        BindingResult re = exception.getBindingResult();
        for (ObjectError error : re.getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append(",");
        }
        errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
        log.error(errorMsg.toString(), exception);
        return new IdmResDTO<>(ResultCode.INVALID_PARAM_TYPE.getCode(), errorMsg.toString());
    }

    @Bean
    public javax.validation.Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        return validatorFactory.getValidator();

    }

    /**
     * function: ConstraintViolationException异常处理
     *
     * @auther 参数校验全局异常处理
     * @date 2020/7/30 3:12 下午
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object defaultErrorHandler(ConstraintViolationException exception) {
        log.error(ResultCode.INVALID_PARAM_TYPE.getMessage(), exception);
        String message = exception.getMessage();
        return new IdmResDTO<>(ResultCode.INVALID_PARAM_TYPE, message);
    }

    /**
     * function: BusinessException异常处理
     *
     * @auther sunking
     * @date 2020/7/30 3:12 下午
     */
    @ExceptionHandler(value = BusinessException.class)
    public Object defaultErrorHandler(BusinessException exception) {
        String message = exception.getErrorMessage();
        String code = exception.getErrorCode();
        log.error("异常信息：", exception);
        return new IdmResDTO<>(code, message);
    }

    /**
     * function: 参数校验异常处理
     *
     * @auther sunking
     * @date 2020/7/30 3:07 下午
     */
    @ExceptionHandler(value = ValidationException.class)
    public Object defaultErrorHandler(ValidationException exception) {
        log.error("", exception);
        String code = ResultCode.INVALID_PARAM_TYPE.getCode();
        String message = ResultCode.INVALID_PARAM_TYPE.getMessage() + ":";
        return new IdmResDTO<>(code, message);
    }

    /**
     * 请求参数类型不合法,请求反序列化，参数类型转换错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Object defaultErrorHandler(HttpMessageNotReadableException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof XssException) {
            log.error(ResultCode.CONTAIN_SENSITIVITY_ERROR.getMessage(), e);
            return new IdmResDTO<>(ResultCode.CONTAIN_SENSITIVITY_ERROR);
        }
        log.error(ResultCode.INVALID_PARAM_TYPE.getMessage(), e);
        if (rootCause instanceof BusinessException) {
            BusinessException businessException = (BusinessException) rootCause;
            return new IdmResDTO<>(businessException.getErrorCode(),businessException.getMessage()+businessException.getErrorMessage());
        }
        return new IdmResDTO<>(ResultCode.INVALID_PARAM_TYPE);
    }

    /**
     * BindException: org.springframework.validation.BeanPropertyBindingResult:异常处理
     * fromData参数校验
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public Object bindException(BindException e){
        List<String> errorMsg = new ArrayList<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String defaultMessage = fieldError.getDefaultMessage();
            errorMsg.add(defaultMessage);
        }
        log.error(errorMsg.toString(), e);
        return new IdmResDTO<>(ResultCode.INVALID_PARAM_TYPE.getCode(), errorMsg.get(0));
    }

}
