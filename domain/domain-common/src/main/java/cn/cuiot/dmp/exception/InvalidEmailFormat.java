package cn.cuiot.dmp.exception;

import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/9/1 00:34
 * @Version V1.0
 */
@ToString
public class InvalidEmailFormat extends DomainException {
    /**
     * 原值
     */
    private final String value;

    public InvalidEmailFormat(String value) {
        super(String.format("邮箱格式不正确：%s", value));
        this.value = value;
    }
}
