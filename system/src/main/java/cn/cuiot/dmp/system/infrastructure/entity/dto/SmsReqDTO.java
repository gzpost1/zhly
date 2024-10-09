package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author liuxue
 * @classname SmsReqDTO
 * @description 登录发送短信请求DTO
 * @date 2023-02-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsReqDTO {

    /**
     * 用户账号，可能是用户名或手机号或邮箱
     */
    private String userAccount;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 图形验证码sid
     */
    private String sid;

    /**
     * 密钥kid
     */
    private String kid;

    /**
     * 密码
     */
    private String password;


}
