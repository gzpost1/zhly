package cn.cuiot.dmp.app.service;

import cn.cuiot.dmp.app.converter.AppUserConverter;
import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.entity.UserEntity;
import cn.cuiot.dmp.app.mapper.AppUserMapper;
import cn.cuiot.dmp.util.Sm4;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private AppUserConverter appUserConverter;

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
     * 根据openid获取用户列表
     */
    public List<AppUserDto> selectUserByOpenid(String openid) {
        List<AppUserDto> list = appUserMapper.selectUserByOpenid(openid);
        if (CollectionUtils.isNotEmpty(list)) {
            for(AppUserDto dto:list){
                if (StringUtils.isNotBlank(dto.getPhoneNumber())) {
                    dto.setPhoneNumber(Sm4.decrypt(dto.getPhoneNumber()));
                }
            }
            return list;
        }
        return Lists.newArrayList();
    }

    /**
     * 根据ID获取用户信息
     */
    public AppUserDto getUserById(Long userId) {
        AppUserDto dto = null;
        UserEntity userEntity = appUserMapper.selectById(userId);
        if (Objects.nonNull(userEntity)) {
            dto = appUserConverter.toAppUserDto(userEntity);
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

    /**
     * 获取user对应的pk_org_id
     */
    public Long getOrgId(Long pkUserId) {
        return appUserMapper.getOrgId(pkUserId);
    }

    /**
     * 找到用户所对应的组织
     */
    public String getDeptId(String pkUserId, String pkOrgId) {
        return appUserMapper.getDeptId(pkUserId, pkOrgId);
    }

    /**
     * 设置用户头像与昵称
     */
    public void setSampleUserInfo(Long userId, String nickName, String avatarUrl) {
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(userId);
        updateEntity.setName(nickName);
        updateEntity.setAvatar(avatarUrl);
        appUserMapper.updateById(updateEntity);
    }

    /**
     * 修改手机号
     */
    public void changePhone(Long userId, String phoneNumber) {
        String encryptedPhone = Sm4.encryption(phoneNumber);
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(userId);
        updateEntity.setPhoneNumber(encryptedPhone);
        appUserMapper.updateById(updateEntity);
    }

}
