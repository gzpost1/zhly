package cn.cuiot.dmp.system.infrastructure.config;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 犬豪
 * @Date 2023/10/8 10:13
 * @Version V1.0
 */
@Slf4j
@RestControllerAdvice
@Order(0)
public class DomainExceptionAdvice {

    @ExceptionHandler(value = DomainException.class)
    public Object defaultErrorHandler(DomainException exception) {
        String message = exception.getMessage();
        log.error("异常信息：", exception);
        return new IdmResDTO<>((String) null, message);
    }
}
