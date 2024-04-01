package cn.cuiot.dmp.upload.domain.exception;

import cn.cuiot.dmp.exception.DomainException;
import lombok.ToString;

/**
 * OSS异常类
 * @author: wuyongchong
 * @date: 2024/4/1 20:08
 */
@ToString
public class OssException extends DomainException {

    /**
     * 原值
     */
    private final String value;

    public OssException(String value) {
        super(value);
        this.value = value;
    }
}
