package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author jiangze
 * @classname ResetPasswordReqDTO
 * @description 重置密码请求DTO
 * @date 2020-07-14
 */
@Data
public class ResetPasswordReqDTO {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String passwordAgain;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 会话id
     */
    private String sid;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码文本
     */
    private String emailCode;

}
