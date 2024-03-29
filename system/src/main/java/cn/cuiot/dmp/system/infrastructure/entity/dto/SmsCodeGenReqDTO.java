package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author jiangze
 * @classname SmsCodeReqDTO
 * @description 短信验证码请求DTO
 * @date 2020-07-09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsCodeGenReqDTO {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 是否为已注册用户
     */
    private Boolean registered;

    /**
     * 图形验证码的会话id
     */
    private String sid;

    /**
     * true代表注册个人小程序，false代表其他场景
     */
    private boolean type = false;

    /**
     * 小区id
     */
    private String communityId;

}
