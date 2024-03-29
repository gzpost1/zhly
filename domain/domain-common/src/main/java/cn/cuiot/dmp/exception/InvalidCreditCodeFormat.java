package cn.cuiot.dmp.exception;

import lombok.ToString;

/**
 * @Description 手机号格式不合规范
 * @Author 犬豪
 * @Date 2023/8/30 09:19
 * @Version V1.0
 */
@ToString
public class InvalidCreditCodeFormat extends DomainException {

    /**
     * 原值
     */
    private final String value;

    public InvalidCreditCodeFormat(String value) {
        super(String.format("社会信用代码（纳税识别号）格式不正确：%s", value));
        this.value = value;
    }
}
