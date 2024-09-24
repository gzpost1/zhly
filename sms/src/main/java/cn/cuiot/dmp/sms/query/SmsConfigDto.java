package cn.cuiot.dmp.sms.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 短信配置
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Data
public class SmsConfigDto {

    /**
     * 密钥名称
     */
    @NotBlank(message = "secretName不能为空")
    private String secretName;

    /**
     * 密钥key
     */
    @NotBlank(message = "secretKey不能为空")
    private String secretKey;
}
