package cn.cuiot.dmp.sms.query;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 短信签名
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Data
public class SmsSignCreateDto {

    /**
     * 签名
     */
    @NotBlank(message = "签名不能为空")
    @Length(min = 1, max = 22, message = "签名长度不能超过22字")
    private String sign;
}
