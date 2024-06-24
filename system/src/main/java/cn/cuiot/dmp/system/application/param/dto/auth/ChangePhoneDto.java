package cn.cuiot.dmp.system.application.param.dto.auth;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/5/23 15:02
 */
@Data
public class ChangePhoneDto implements Serializable {

    /**
     * 原手机号验证码
     */
    @NotBlank(message = "原手机号验证码不能为空")
    private String preSmsCode;

    /**
     * 新手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 新手机号验证码
     */
    @NotBlank(message = "手机号验证码不能为空")
    private String smsCode;

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
