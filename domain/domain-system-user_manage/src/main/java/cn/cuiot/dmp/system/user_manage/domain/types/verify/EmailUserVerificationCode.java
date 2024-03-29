package cn.cuiot.dmp.system.user_manage.domain.types.verify;

import cn.cuiot.dmp.domain.types.Email;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @Description 邮箱验证码
 * @Author 犬豪
 * @Date 2023/8/30 14:30
 * @Version V1.0
 */
@ToString(callSuper = true)
@Getter
public class EmailUserVerificationCode extends UserVerificationCode {

    public EmailUserVerificationCode(@NonNull String verificationCode, @NonNull String sid, @NonNull Email email) {
        super(verificationCode, sid);
        this.email = email;
    }

    private Email email;

}
