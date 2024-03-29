package cn.cuiot.dmp.exception;

import lombok.ToString;

/**
*
* @author zhangjg
* @date 2024/1/11 15:18
*/
@ToString
public class InvalidPlateNumberFormat extends DomainException {
    /**
     * 原值
     */
    private final String value;


    public InvalidPlateNumberFormat(String value) {
        super(String.format("车牌号码格式不正确：%s", value));
        this.value = value;
    }
}
