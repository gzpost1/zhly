package cn.cuiot.dmp.exception;

import lombok.ToString;

/**
 * @author jiangze
 * @classname InvalidAesContent
 * @description 加密内容不合法
 * @date 2023-11-08
 */
@ToString
public class InvalidAesContent extends DomainException {
    /**
     * 原值
     */
    private final String value;

    public InvalidAesContent(String value) {
        super(String.format("加密格式不正确：%s", value));
        this.value = value;
    }
}
