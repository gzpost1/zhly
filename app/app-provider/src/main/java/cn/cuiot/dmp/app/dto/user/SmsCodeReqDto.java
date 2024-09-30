package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotNull;

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

    /**
     * 用户身份（1-员工 2-业主）
     */
    @NotNull(message = "用户身份参数不能为空")
    private Integer userType;
}
