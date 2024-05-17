package cn.cuiot.dmp.system.domain.exception.verify;

import cn.cuiot.dmp.exception.DomainException;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationCode;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/8/30 16:20
 * @Version V1.0
 */
@ToString
public class CodeVerifyFail extends DomainException {

    private final UserVerificationCode userVerificationCode;

    public CodeVerifyFail(UserVerificationCode userVerificationCode) {
        this.userVerificationCode = userVerificationCode;
    }

}
