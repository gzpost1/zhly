package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 手机号登录参数
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:38
 */
@Data
public class PhoneLoginDto implements Serializable {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    /**
     * 用户身份（1-员工 2-业主）
     */
    @NotNull(message = "用户身份参数不能为空")
    private Integer userType;

    /**
     * 小程序openid
     */
    private String openid;

    /**
     * IP地址-前端不用管
     */
    private String ipAddr;
}
