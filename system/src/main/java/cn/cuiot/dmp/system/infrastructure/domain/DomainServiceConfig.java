package cn.cuiot.dmp.system.infrastructure.domain;

import cn.cuiot.dmp.system.user_manage.domain.service.UserDomainService;
import cn.cuiot.dmp.system.user_manage.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.user_manage.domain.service.VerifyDomainService;
import cn.cuiot.dmp.system.user_manage.domain.service.impl.UserDomainServiceImpl;
import cn.cuiot.dmp.system.user_manage.domain.service.impl.UserPhoneNumberDomainServiceImpl;
import cn.cuiot.dmp.system.user_manage.domain.service.impl.VerifyDomainServiceImpl;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationCode;
import cn.cuiot.dmp.system.user_manage.external.VerificationCodeValidator;
import cn.cuiot.dmp.system.user_manage.messaging.UserDomainEventSender;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 领域服务注册
 * @Author 犬豪
 * @Date 2023/8/30 09:06
 * @Version V1.0
 */
@Configuration
public class DomainServiceConfig {
    /**
     * 用户电话领域服务
     */
    @Bean
    public UserPhoneNumberDomainService userPhoneNumberDomainService(UserRepository userRepository) {
        return new UserPhoneNumberDomainServiceImpl(userRepository);
    }

    /**
     * 用户领域服务相关配置
     */
    @Configuration
    public static class UserDomainServiceConfig {

        /**
         * 用户领域服务
         */
        @Bean
        public UserDomainService userDomainService(UserRepository userRepository,
            VerifyDomainService verifyDomainService, UserDomainEventSender userDomainEventSender) {
            return new UserDomainServiceImpl(userRepository, verifyDomainService, userDomainEventSender);
        }

        /**
         * 验证服务注册
         */
        @Bean
        public VerifyDomainService verifyDomainService(
            List<VerificationCodeValidator<? extends VerificationCode>> verificationCodeValidatorList) {
            return new VerifyDomainServiceImpl(verificationCodeValidatorList);
        }

    }

}
