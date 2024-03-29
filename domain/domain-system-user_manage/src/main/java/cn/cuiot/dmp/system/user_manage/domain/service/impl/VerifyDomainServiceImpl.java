package cn.cuiot.dmp.system.user_manage.domain.service.impl;

import cn.cuiot.dmp.system.user_manage.domain.service.VerifyDomainService;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationCode;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.VerificationResult;
import cn.cuiot.dmp.system.user_manage.exception.verify.InitVerificationCodeValidatorsError;
import cn.cuiot.dmp.system.user_manage.exception.verify.NotSupportVerificationCode;
import cn.cuiot.dmp.system.user_manage.external.VerificationCodeValidator;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/30 14:34
 * @Version V1.0
 */
public class VerifyDomainServiceImpl implements VerifyDomainService {

    /**
     * list保证顺序
     */
    private List<VerificationCodeValidator<? extends VerificationCode>> verificationCodeValidators;
    private Map<VerificationCodeValidator<? extends VerificationCode>,
        Class<? extends VerificationCode>> verificationCodeValidatorClassMap;

    public VerifyDomainServiceImpl(
        @NonNull List<VerificationCodeValidator<? extends VerificationCode>> verificationCodeValidators) {
        initVerificationCodeValidators(verificationCodeValidators);
    }

    private void initVerificationCodeValidators(
        List<VerificationCodeValidator<? extends VerificationCode>> verificationCodeValidators) {
        this.verificationCodeValidators = verificationCodeValidators;
        verificationCodeValidatorClassMap =
            verificationCodeValidators.stream().collect(Collectors.toMap(i -> i, this::getGenericParameterClassType));
    }

    private Class<? extends VerificationCode>
        getGenericParameterClassType(VerificationCodeValidator<? extends VerificationCode> verificationCodeValidator) {
        // 获得带有泛型的父类
        Type[] genericInterfaces = verificationCodeValidator.getClass().getGenericInterfaces();
        Type genericInterface = genericInterfaces[0];
        if (genericInterface instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)genericInterface;
            // 获得VerificationCodeValidator<String>，<>中的实际类型参数
            return (Class<? extends VerificationCode>)parameterizedType.getActualTypeArguments()[0];
        }
        throw new InitVerificationCodeValidatorsError();
    }

    @Override
    public <R extends VerificationResult, T extends VerificationCode> R verify(@NonNull T verificationCode) {
        for (VerificationCodeValidator verificationCodeValidator : verificationCodeValidators) {
            // 获取支持处理的VerificationCode具体Class类型
            Class<? extends VerificationCode> supportClass =
                verificationCodeValidatorClassMap.get(verificationCodeValidator);
            if (supportClass.isAssignableFrom(verificationCode.getClass())
                && verificationCodeValidator.support(verificationCode)) {
                return (R)verificationCodeValidator.verify(verificationCode);
            }
        }
        // 不支持处理这个verificationCode
        throw new NotSupportVerificationCode(verificationCode);
    }

    @Override
    public <T extends VerificationCode> boolean deleteVerificationCode(@NonNull T verificationCode) {
        for (VerificationCodeValidator verificationCodeValidator : verificationCodeValidators) {
            // 获取支持处理的VerificationCode具体Class类型
            Class<? extends VerificationCode> supportClass =
                verificationCodeValidatorClassMap.get(verificationCodeValidator);
            if (supportClass.isAssignableFrom(verificationCode.getClass())
                && verificationCodeValidator.support(verificationCode)) {
                return verificationCodeValidator.deleteVerificationCode(verificationCode);
            }
        }
        // 不支持处理这个verificationCode
        throw new NotSupportVerificationCode(verificationCode);
    }
}
