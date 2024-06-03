package cn.cuiot.dmp.system.domain.service.impl;

import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.domain.query.UserCommonQuery;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import java.util.Arrays;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/30 09:03
 * @Version V1.0
 */
public class UserPhoneNumberDomainServiceImpl implements UserPhoneNumberDomainService {
    private UserRepository userRepository;

    public UserPhoneNumberDomainServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean judgePhoneNumberAlreadyExists(@NonNull PhoneNumber phoneNumber) {
        User user = userRepository.commonQueryOne(UserCommonQuery.builder().phoneNumber(phoneNumber).build());
        return user != null;
    }

    @Override
    public boolean judgePhoneNumberAlreadyExists(@NonNull PhoneNumber phoneNumber,
        @NonNull UserTypeEnum... userTypeEnum) {
        User user = userRepository.commonQueryOne(
            UserCommonQuery.builder().phoneNumber(phoneNumber).userTypeEnumList(Arrays.asList(userTypeEnum)).build());
        return user != null;
    }
}
