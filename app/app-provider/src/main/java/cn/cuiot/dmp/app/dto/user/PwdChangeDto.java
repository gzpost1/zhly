package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改密码参数
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:38
 */
@Data
public class PwdChangeDto implements Serializable {
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
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 临时密钥id
     */
    @NotBlank(message = "密钥id不能为空")
    private String kid;

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
