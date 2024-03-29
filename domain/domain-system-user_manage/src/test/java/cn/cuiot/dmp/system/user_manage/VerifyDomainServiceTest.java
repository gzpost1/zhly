package cn.cuiot.dmp.system.user_manage;

import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.system.user_manage.domain.service.impl.VerifyDomainServiceImpl;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.EmailUserVerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.PhoneUserVerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationResult;
import cn.cuiot.dmp.system.user_manage.external.VerificationCodeValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/8/30 14:54
 * @Version V1.0
 */
public class VerifyDomainServiceTest {

    public static class EmailVerificationCodeValidator implements VerificationCodeValidator<EmailUserVerificationCode> {

        @Override
        public boolean support(EmailUserVerificationCode verificationCode) {
            return verificationCode.getEmail().decrypt().equals("aabb@qq.com");
        }

        @Override
        public VerificationResult verify(EmailUserVerificationCode verificationCode) {
            return new VerificationResult(true);
        }
    }

    public static class PhoneVerificationCodeValidator implements VerificationCodeValidator<PhoneUserVerificationCode> {

        @Override
        public boolean support(PhoneUserVerificationCode verificationCode) {
            return true;
        }

        @Override
        public VerificationResult verify(PhoneUserVerificationCode verificationCode) {
            return new VerificationResult(true);
        }
    }

    @Test
    public void testVerificationCodeValidatorInit() {
        List<VerificationCodeValidator<? extends VerificationCode>> list = new ArrayList();
        list.add(new EmailVerificationCodeValidator());
        list.add(new PhoneVerificationCodeValidator());
        VerifyDomainServiceImpl verifyDomainService = new VerifyDomainServiceImpl(list);

        VerificationResult verify =
            verifyDomainService.verify(new EmailUserVerificationCode("123", "sid", new Email("aabb@qq.com")));
        System.out.println(verify.isPass());
        verify =
            verifyDomainService.verify(new PhoneUserVerificationCode("12313", "123", new PhoneNumber("18115612233")));
        System.out.println(verify.isPass());
    }
}
