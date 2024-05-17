package cn.cuiot.dmp.system.infrastructure.domain.verify;

import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_EXPIRED_ERROR;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.types.verify.PhoneUserVerificationCode;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationResult;
import cn.cuiot.dmp.system.domain.external.VerificationCodeValidator;
import cn.cuiot.dmp.system.domain.query.UserCommonQuery;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
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
public class PhoneVerificationCodeValidator implements VerificationCodeValidator<PhoneUserVerificationCode> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean support(PhoneUserVerificationCode verificationCode) {
        return true;
    }

    @Override
    public UserVerificationResult verify(PhoneUserVerificationCode phoneVerificationCode) {
        String uuid = phoneVerificationCode.getSid();
        PhoneNumber phoneNumber = phoneVerificationCode.getPhoneNumber();
        String verificationCode = phoneVerificationCode.getCode();
        String expectedSmsText = stringRedisTemplate.opsForValue().get(CacheConst.SMS_CODE_TEXT_REDIS_KEY + uuid);

        if (StringUtils.isEmpty(expectedSmsText)) {
            // 短信验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        if (!expectedSmsText.equals(phoneNumber.decrypt() + verificationCode)) {
            throw new BusinessException(SMS_CODE_ERROR);
        }

        User user = userRepository.commonQueryOne(UserCommonQuery.builder().phoneNumber(phoneNumber).build());

        return new UserVerificationResult(true, user);
    }

    @Override
    public boolean deleteVerificationCode(PhoneUserVerificationCode verificationCode) {
        // 删除验证码
        return Boolean.TRUE
            .equals(stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY + verificationCode.getSid()));
    }
}
