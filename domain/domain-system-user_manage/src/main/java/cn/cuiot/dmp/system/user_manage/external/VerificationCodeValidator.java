package cn.cuiot.dmp.system.user_manage.external;

import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationResult;

/**
 * @Description 验证码验证器
 * @Author 犬豪
 * @Date 2023/8/30 14:39
 * @Version V1.0
 */
public interface VerificationCodeValidator<T extends VerificationCode> {

    /**
     * 是否支持处理
     */
    boolean support(T verificationCode);

    /**
     * 验证
     */
    <R extends VerificationResult> R verify(T verificationCode);

    /**
     * 删除验证信息
     */
    default boolean deleteVerificationCode(T verificationCode) {
        return true;
    }

}
