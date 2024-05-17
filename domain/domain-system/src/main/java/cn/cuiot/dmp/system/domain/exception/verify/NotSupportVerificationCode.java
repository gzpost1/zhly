package cn.cuiot.dmp.system.domain.exception.verify;

import cn.cuiot.dmp.exception.DomainException;
import cn.cuiot.dmp.system.domain.types.verify.VerificationCode;
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
