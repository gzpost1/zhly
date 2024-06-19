package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 密码登录参数
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:38
 */
@Data
public class PwdLoginDto implements Serializable {

    /**
     * 用户名或手机号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 图形验证码
     */
    private String kaptchaText;

    /**
     * 图形验证码sid
     */
    private String sid;

    /**
     * 临时密钥id
     */
    private String kid;

    /**
     * 小程序openid
     */
    private String openid;

    /**
     * 用户身份（1-员工 2-业主）
     */
    @NotNull(message = "用户身份参数不能为空")
    private Integer userType;

    /**
     * IP地址-前端不用管
     */
    private String ipAddr;

}
