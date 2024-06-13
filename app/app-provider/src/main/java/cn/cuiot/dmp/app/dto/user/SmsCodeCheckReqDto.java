package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 手机短信验证码校验参数
 * @author: wuyongchong
 * @date: 2024/5/24 11:39
 */
@Data
public class SmsCodeCheckReqDto implements Serializable {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
