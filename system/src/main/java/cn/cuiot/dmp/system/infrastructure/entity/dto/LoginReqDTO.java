package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author jiangze
 * @classname LoginReqDTO
 * @description 用户登录请求DTO
 * @date 2020-06-30
 */
@Data
public class LoginReqDTO {

    /**
     * 用户账号，可能是用户名或手机号或邮箱
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 图形验证码sid
     */
    private String sid;

    /**
     * 临时密钥id
     */
    private String kid;

    /**
     * 是否同意隐私协议（0：未同意、1：同意)
     */
    //private String privacyAgreement;

    /**
     * 短信验证码
     */
    //private String smsCode;

    /**
     * 小程序openid
     */
    private String openid;
}
