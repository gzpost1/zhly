package cn.cuiot.dmp.system.domain.service.impl;

import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.event.ResetPasswordSuccessEvent;
import cn.cuiot.dmp.system.domain.service.UserDomainService;
import cn.cuiot.dmp.system.domain.service.VerifyDomainService;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationCode;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationResult;
import cn.cuiot.dmp.system.domain.exception.UserNotExist;
import cn.cuiot.dmp.system.domain.exception.verify.CodeVerifyFail;
import cn.cuiot.dmp.system.domain.messaging.UserDomainEventSender;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/30 11:18
 * @Version V1.0
 */
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;

    private final VerifyDomainService verifyDomainService;

    private final UserDomainEventSender userDomainEventSender;

    public UserDomainServiceImpl(@NonNull UserRepository userRepository,
        @NonNull VerifyDomainService verifyDomainService, @NonNull UserDomainEventSender userDomainEventSender) {
        this.userRepository = userRepository;
        this.verifyDomainService = verifyDomainService;
        this.userDomainEventSender = userDomainEventSender;
    }

    @Override
    public boolean resetPassword(@NonNull UserVerificationCode userVerificationCode, @NonNull Password newPassword) {

        // 用户验证码验证服务
        UserVerificationResult verificationResult = verifyDomainService.verify(userVerificationCode);
        if (!verificationResult.isPass()) {
            throw new CodeVerifyFail(userVerificationCode);
        }

        User user = verificationResult.getUser();
        if (user == null) {
            throw new UserNotExist();
        }

        Password oldPassword = user.getPassword();
        user.setPassword(newPassword);
        user.updatedByPortal();

        boolean success = userRepository.save(user);

        if (success) {
            // 发送事件
            ResetPasswordSuccessEvent event = ResetPasswordSuccessEvent.builder().oldPassword(oldPassword)
                .newPassword(newPassword).user(user).userVerificationCode(userVerificationCode).build();
            userDomainEventSender.publishResetPasswordSuccessEvent(event);
        }

        return success;
    }
}
