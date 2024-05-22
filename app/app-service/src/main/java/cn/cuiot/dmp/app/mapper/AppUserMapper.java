package cn.cuiot.dmp.app.mapper;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.entity.UserEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.IotBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: wuyongchong
 * @date: 2024/5/22 15:18
 */
public interface AppUserMapper extends IotBaseMapper<UserEntity> {

    AppUserDto getUserByPhoneAndUserType(@Param("encryptedPhone") String encryptedPhone, @Param("userType") Integer userType);

}
