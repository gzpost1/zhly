package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 切换身份参数
 * @author: wuyongchong
 * @date: 2024/5/23 15:02
 */
@Data
public class SwitchUserTypeDto implements Serializable {

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

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
