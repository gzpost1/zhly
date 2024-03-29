package cn.cuiot.dmp.system.user_manage.domain.service;

import cn.cuiot.dmp.domain.service.DomainService;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import lombok.NonNull;

/**
 * @Description 用户手机号领域服务
 * @Author 犬豪
 * @Date 2023/8/30 09:01
 * @Version V1.0
 */
public interface UserPhoneNumberDomainService extends DomainService {

    boolean judgePhoneNumberAlreadyExists(@NonNull PhoneNumber phoneNumber);

    boolean judgePhoneNumberAlreadyExists(@NonNull PhoneNumber phoneNumber, @NonNull UserTypeEnum... userTypeEnum);

}
