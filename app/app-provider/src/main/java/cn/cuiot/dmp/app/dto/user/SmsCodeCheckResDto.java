package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 手机短信验证码校验响应
 * @author: wuyongchong
 * @date: 2024/5/24 11:39
 */
@Data
public class SmsCodeCheckResDto implements Serializable {

    /**
     * 是否成功
     */
    private Boolean checkSucceed;
    /**
     * 提示
     */
    private String message;

    public SmsCodeCheckResDto() {
    }

    public SmsCodeCheckResDto(Boolean checkSucceed, String message) {
        this.checkSucceed = checkSucceed;
        this.message = message;
    }
}
