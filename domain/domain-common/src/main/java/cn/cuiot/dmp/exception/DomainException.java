package cn.cuiot.dmp.exception;

/**
 * 领域异常标记接口
 *
 * @Author 犬豪
 * @Date 2023/8/31 09:31
 * @Version V1.0
 */
public class DomainException extends RuntimeException {
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }
}
