package cn.cuiot.dmp.system.user_manage.domain.types.verify;

import cn.cuiot.dmp.domain.types.PhoneNumber;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 手机验证码
 * 
 * @Author 犬豪
 * @Date 2023/8/30 14:27
 * @Version V1.0
 */
@ToString(callSuper = true)
@Getter
public class PhoneUserVerificationCode extends UserVerificationCode {
    public PhoneUserVerificationCode(@NonNull String verificationCode, @NonNull String sid,
                                     @NonNull PhoneNumber phoneNumber) {
        super(verificationCode, sid);
        this.phoneNumber = phoneNumber;
    }

    private PhoneNumber phoneNumber;

}
