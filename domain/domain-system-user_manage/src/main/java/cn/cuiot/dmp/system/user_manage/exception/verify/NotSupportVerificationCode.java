package cn.cuiot.dmp.system.user_manage.exception.verify;

import cn.cuiot.dmp.exception.DomainException;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationCode;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/8/31 09:30
 * @Version V1.0
 */
@ToString
public class NotSupportVerificationCode extends DomainException {
    private VerificationCode verificationCode;

    public NotSupportVerificationCode(VerificationCode userVerificationCode) {
        this.verificationCode = userVerificationCode;
    }
}
