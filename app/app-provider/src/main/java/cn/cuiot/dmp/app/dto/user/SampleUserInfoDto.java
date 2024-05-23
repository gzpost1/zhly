package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户信息参数
 * @author: wuyongchong
 * @date: 2024/5/23 11:53
 */
@Data
public class SampleUserInfoDto implements Serializable {

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 用户ID-前端不用填
     */
    private Long userId;
}
