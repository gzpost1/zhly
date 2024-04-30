package cn.cuiot.dmp.system.domain.types.verify;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 用户验证码
 * 
 * @Author 犬豪
 * @Date 2023/8/30 14:27
 * @Version V1.0
 */
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class UserVerificationCode extends VerificationCode {

    protected UserVerificationCode(@NonNull String verificationCode, String sid) {
        super(verificationCode);
        this.sid = sid;
    }

    /**
     * 会话id
     */
    private String sid;

}
