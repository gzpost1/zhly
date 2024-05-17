package cn.cuiot.dmp.system.domain.service;

import cn.cuiot.dmp.domain.service.DomainService;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationCode;
import lombok.NonNull;

/**
 * @Description 用户领域服务
 * @Author 犬豪
 * @Date 2023/8/30 10:57
 * @Version V1.0
 */
public interface UserDomainService extends DomainService {
    /**
     * 重置密码
     * 
     * @param userVerificationCode
     *            验证码
     * @param newPassword
     *            新密码
     * @return
     */
    boolean resetPassword(@NonNull UserVerificationCode userVerificationCode, @NonNull Password newPassword);
}
