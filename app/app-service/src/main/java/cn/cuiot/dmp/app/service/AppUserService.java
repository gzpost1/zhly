package cn.cuiot.dmp.app.service;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.entity.UserEntity;
import cn.cuiot.dmp.app.mapper.AppUserMapper;
import cn.cuiot.dmp.util.Sm4;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author: wuyongchong
 * @date: 2024/5/22 15:01
 */
@Slf4j
@Service
public class AppUserService {

    @Autowired
    private AppUserMapper appUserMapper;


    /**
     * 根据手机号和用户身份获取用户信息
     */
    public AppUserDto getUserByPhoneAndUserType(String phone, Integer userType) {
        String encryptedPhone = Sm4.encryption(phone);
        AppUserDto dto = appUserMapper.getUserByPhoneAndUserType(encryptedPhone, userType);
        if (Objects.nonNull(dto)) {
            if (StringUtils.isNotBlank(dto.getPhoneNumber())) {
                dto.setPhoneNumber(Sm4.decrypt(dto.getPhoneNumber()));
            }
        }
        return dto;
    }

    /**
     * 创建用户
     */
    public void createAppUser(UserEntity userEntity) {
        appUserMapper.insert(userEntity);
    }

    /**
     * 修改用户
     */
    public void updateAppUser(UserEntity updateEntity) {
        appUserMapper.updateById(updateEntity);
    }

}
