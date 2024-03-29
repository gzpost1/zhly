package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname SmsCodeCheckReqDTO
 * @description 短信验证码验证请求DTO
 * @date 2020-07-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeCheckReqDTO {

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
     * 0:个人账户，1:商企账户
     */
    private String type;

}
