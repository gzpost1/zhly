package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 小程序登录参数
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:38
 */
@Data
public class MiniLoginDto implements Serializable {

    /**
     * 小程序凭证code
     */
    @NotBlank(message = "序凭证code参数不能为空")
    private String code;

    /**
     * 小程序openid
     */
    @NotBlank(message = "小程序openid参数不能为空")
    private String openid;

    /**
     * 用户身份（1-员工 2-业主）
     */
    @NotNull(message = "用户身份参数不能为空")
    private Integer userType;


}
