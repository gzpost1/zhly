package cn.cuiot.dmp.exception;

import lombok.ToString;

/**
 * @Description 手机号格式不合规范
 * @Author 犬豪
 * @Date 2023/8/30 09:19
 * @Version V1.0
 */
@ToString
public class InvalidTelNumberFormat extends DomainException {

    /**
     * 原值
     */
    private final String value;

    public InvalidTelNumberFormat(String value) {
        super(String.format("座机号码格式不正确：%s。（中国座机号码格式由区号和号码组成，区号由3位数或4位数字组成，号码由7位数字组成）", value));
        this.value = value;
    }
}
