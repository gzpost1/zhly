package cn.cuiot.dmp.system.infrastructure.domain.verify;

import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.PhoneUserVerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.UserVerificationResult;
import cn.cuiot.dmp.system.user_manage.external.VerificationCodeValidator;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @Author 犬豪
 * @Date 2023/8/30 17:20
 * @Version V1.0
 */
@ConditionalOnProperty(name = "self.debug", havingValue = "true")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DebugPhonelUserVerificationCodeValidator implements VerificationCodeValidator<PhoneUserVerificationCode> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean support(PhoneUserVerificationCode verificationCode) {
        return true;
    }

    @Override
    public UserVerificationResult verify(PhoneUserVerificationCode verificationCode) {
        User user = userRepository.commonQueryOne(UserCommonQuery.builder().phoneNumber(verificationCode.getPhoneNumber()).build());
        return new UserVerificationResult(true, user);
    }

}
