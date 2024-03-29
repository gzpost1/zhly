package cn.cuiot.dmp.system.infrastructure.domain.verify;

import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_EXPIRED_ERROR;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.EmailUserVerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.UserVerificationResult;
import cn.cuiot.dmp.system.user_manage.external.VerificationCodeValidator;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author 犬豪
 * @Date 2023/8/30 18:01
 * @Version V1.0
 */
@Component
public class EmailVerificationCodeValidator implements VerificationCodeValidator<EmailUserVerificationCode> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean support(EmailUserVerificationCode verificationCode) {
        return true;
    }

    @Override
    public UserVerificationResult verify(EmailUserVerificationCode emailVerificationCode) {
        Email email = emailVerificationCode.getEmail();
        String verificationCode = emailVerificationCode.getCode();

        String expectedEmailText = stringRedisTemplate.opsForValue()
            .get(CacheConst.EMAIL_CODE_TEXT_REDIS_KEY_FOR_RESET_PASSWORD + email.decrypt());
        if (StringUtils.isEmpty(expectedEmailText)) {
            // 邮箱验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        if (!expectedEmailText.equals(verificationCode)) {
            throw new BusinessException(SMS_CODE_ERROR);
        }

        User user = userRepository.commonQueryOne(UserCommonQuery.builder().email(email).build());
        return new UserVerificationResult(true, user);
    }

    @Override
    public boolean deleteVerificationCode(EmailUserVerificationCode verificationCode) {
        return Boolean.TRUE.equals(stringRedisTemplate
            .delete(CacheConst.EMAIL_CODE_TEXT_REDIS_KEY_FOR_RESET_PASSWORD + verificationCode.getEmail().decrypt()));
    }
}
