package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author jiangze
 * @classname SmsCodeCheckReqDTO
 * @description 短信验证码验证请求DTO
 * @date 2020-07-13
 */
@Data
public class SmsCodeCheckStrongerReqDTO {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 短信验证码会话id
     */
    private String sid;

    /**
     * 短信验证码
     */
    private String smsCodeOld;
    

}
