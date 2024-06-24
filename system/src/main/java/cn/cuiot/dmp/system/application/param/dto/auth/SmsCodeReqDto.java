package cn.cuiot.dmp.system.application.param.dto.auth;

import java.io.Serializable;
import lombok.Data;

/**
 * 获取手机短信验证码参数
 * @author: wuyongchong
 * @date: 2024/5/24 11:39
 */
@Data
public class SmsCodeReqDto implements Serializable {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 图形验证码的会话id
     */
    private String sid;

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
