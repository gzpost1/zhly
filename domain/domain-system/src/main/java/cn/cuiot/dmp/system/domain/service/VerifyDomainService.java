package cn.cuiot.dmp.system.domain.service;

import cn.cuiot.dmp.domain.service.DomainService;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationCode;
import cn.cuiot.dmp.system.domain.types.verify.UserVerificationResult;
import cn.cuiot.dmp.system.domain.types.verify.VerificationCode;
import cn.cuiot.dmp.system.domain.types.verify.VerificationResult;
import lombok.NonNull;

/**
 * @Description 验证服务
 * @Date 2023/8/30 11:05
 * @Version V1.0
 */
public interface VerifyDomainService extends DomainService {

    /**
     * 验证码验证服务
     * 
     * @param verificationCode
     *            需要验证的Code
     * 
     * @return 用户验证结果
     * @param <R>
     *            {@link UserVerificationResult} 用户验证结果，验证通过返回用户实体
     * @param <T>
     *            {@link UserVerificationCode}. 用户验证服务
     */
    <R extends VerificationResult, T extends VerificationCode> R verify(@NonNull T verificationCode);

    /**
     * 删除验证码
     */
    <T extends VerificationCode> boolean deleteVerificationCode(@NonNull T verificationCode);
}
