package cn.cuiot.dmp.system.infrastructure.domain.converter;

import cn.cuiot.dmp.base.infrastructure.domain.converter.Converter;
import cn.cuiot.dmp.domain.types.Address;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.EncryptedValue;
import cn.cuiot.dmp.domain.types.IP;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.types.enums.UserStatusEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 领域模型和数据库模型之间的转换
 * @Author 犬豪
 * @Date 2023/8/28 17:01
 * @Version V1.0
 */
@Slf4j
public class UserConverter implements Converter<User, UserEntity> {

    private UserConverter() {
    }

    public static final UserConverter INSTANCE = new UserConverter();

    @Override
    public User toDomain(UserEntity userEntity) {
        try {
            if (userEntity == null) {
                return null;
            }
            User user = User.builder().build();
            user.setId(userEntity.getId() != null ? new UserId(userEntity.getId()) : null);
            user.setUsername(userEntity.getUsername());
            if (userEntity.getPassword() != null) {
                Password password = new Password();
                password.setHashEncryptValue(userEntity.getPassword());
                user.setPassword(password);
            }
            if (StringUtils.isNotBlank(userEntity.getEmail())) {
                user.setEmail(new Email(new EncryptedValue(userEntity.getEmail())));
            }

            // 字符串为""认为是null
            if (StringUtils.isNotBlank(userEntity.getPhoneNumber())) {
                user.setPhoneNumber(new PhoneNumber(new EncryptedValue(userEntity.getPhoneNumber())));
            }

            user.setAvatar(userEntity.getAvatar());
            user.setStatus(UserStatusEnum.valueOf(userEntity.getStatus()));
            if (userEntity.getLastOnlineIp() != null) {
                user.setLastOnlineIp(new IP(userEntity.getLastOnlineIp()));
            }
            if (userEntity.getLastOnlineAddress() != null) {
                user.setLastOnlineAddress(new Address(userEntity.getLastOnlineAddress()));
            }

            user.setName(userEntity.getName());
            user.setPostId(userEntity.getPostId());
            user.setRemark(userEntity.getRemark());
            user.setOpenid(userEntity.getOpenid());

            user.setLastOnlineOn(userEntity.getLastOnlineOn());
            user.setCreatedOn(userEntity.getCreatedOn());
            user.setCreatedBy(userEntity.getCreatedBy());
            user.setCreatedByType(OperateByTypeEnum.valueOf(userEntity.getCreatedByType()));
            user.setUpdatedOn(userEntity.getUpdatedOn());
            user.setUpdatedBy(userEntity.getUpdatedBy());
            user.setUpdatedByType(OperateByTypeEnum.valueOf(userEntity.getUpdatedByType()));
            user.setDeletedFlag(userEntity.getDeletedFlag());
            user.setUserType(UserTypeEnum.valueOf(userEntity.getUserType()));
            user.setContactPerson(userEntity.getContactPerson());
            if (userEntity.getContactAddress() != null) {
                user.setContactAddress(new Address(userEntity.getContactAddress()));
            }
            user.setLongTimeLogin(userEntity.getLongTimeLogin());
            return user;
        } catch (Exception e) {
            log.error("userId：{}", userEntity.getId(), e);
            throw e;
        }
    }

    @Override
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(user.getId() != null ? user.getId().getValue() : null);
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword() !=null ? user.getPassword().getHashEncryptValue() : null);
        entity.setEmail(user.getEncryptedEmail());
        entity.setPhoneNumber(user.getEncryptedPhoneNumber());
        entity.setAvatar(user.getAvatar());
        entity.setStatus(user.getStatus() != null ? user.getStatus().getValue() : null);
        entity.setLastOnlineIp(user.getLastOnlineIpStr());
        entity.setLastOnlineAddress(user.getLastOnlineAddressStr());
        entity.setLastOnlineOn(user.getLastOnlineOn());
        entity.setCreatedOn(user.getCreatedOn());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setCreatedByType(user.getCreatedByType() != null ? user.getCreatedByType().getValue() : null);
        entity.setUpdatedOn(user.getUpdatedOn());
        entity.setUpdatedBy(user.getUpdatedBy());
        entity.setUpdatedByType(user.getUpdatedByType() != null ? user.getUpdatedByType().getValue() : null);
        entity.setDeletedFlag(user.getDeletedFlag());
        entity.setUserType(user.getUserType() != null ? user.getUserType().getValue() : null);
        entity.setContactPerson(user.getContactPerson());
        entity.setContactAddress(user.getContactAddressStr());
        entity.setLongTimeLogin(user.getLongTimeLogin());

        entity.setName(user.getName());
        entity.setPostId(user.getPostId());
        entity.setRemark(user.getRemark());
        entity.setOpenid(user.getOpenid());

        return entity;
    }
}
