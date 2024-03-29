package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @Description 发送短信验证码（用新的手机号）
 * @Author cds
 * @Date 2020/8/19
 */
@Data
public class SmsCodeByNewReqDTO {

    /**
     * 旧的手机号
     */
    private String oldPhoneNumber;

    /**
     * 原来的短信验证码
     */
    private String smsCode;

    /**
     * 原来的邮箱验证码
     */
    private String emailCode;

    /**
     * 新的手机号
     */
    private String newPhoneNumber;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 图形验证码的会话id
     */
    private String sid;
}
