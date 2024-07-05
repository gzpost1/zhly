package cn.cuiot.dmp.system.application.service;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_OLD_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.system.application.param.dto.auth.ChangePhoneDto;
import cn.cuiot.dmp.system.application.param.dto.auth.PwdChangeDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SampleUserInfoDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeCheckResDto;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntityMapper;
import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2024/6/24 15:03
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置用户头像与昵称
     */
    public void setSampleUserInfo(SampleUserInfoDto dto) {
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(dto.getUserId());
        updateEntity.setName(dto.getNickName());
        updateEntity.setAvatar(dto.getAvatarUrl());
        userEntityMapper.updateById(updateEntity);
    }

    /**
     * 根据ID获取用户信息
     */
    public UserEntity getUserById(Long userId) {
        UserEntity userEntity = userEntityMapper.selectById(userId);
        if (Objects.nonNull(userEntity)) {
            if (StringUtils.isNotBlank(userEntity.getPhoneNumber())) {
                userEntity.setPhoneNumber(Sm4.decrypt(userEntity.getPhoneNumber()));
            }
        }
        return userEntity;
    }

    /**
     * 修改手机号
     */
    public void changePhone(ChangePhoneDto dto) {
        UserEntity userEntity = getUserById(dto.getUserId());
        String oldPhone = Optional.ofNullable(userEntity).map(d -> d.getPhoneNumber()).orElse(null);
        if (dto.getPhoneNumber().equals(oldPhone)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "新手机号不能与旧手机号一致");
        }
        SmsCodeCheckResDto res = verifyService
                .checkPhoneSmsCode(oldPhone, dto.getUserId(), dto.getPreSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_OLD_INVALID);
        }
        res = verifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId(), dto.getSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_ERROR);
        }
        String encryptedPhone = Sm4.encryption(dto.getPhoneNumber());
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(dto.getUserId());
        updateEntity.setPhoneNumber(encryptedPhone);
        userEntityMapper.updateById(updateEntity);
    }

    /**
     * 修改密码
     */
    public void changePwd(PwdChangeDto dto) {
        /**
         * 手机号校验
         */
        if (StringUtils.isBlank(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY, "请输入手机号");
        }
        if (!PhoneUtil.isPhone(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入11位手机号码");
        }
        // 密码参数校验
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_IS_EMPTY, "请输入密码");
        }
        //临时Aes密钥ID参数校验
        if (StringUtils.isBlank(dto.getKid())) {
            throw new BusinessException(ResultCode.KID_IS_EMPTY, "密钥ID为空");
        }
        /**
         * 短信验证码参数校验
         */
        if (StringUtils.isBlank(dto.getSmsCode())) {
            throw new BusinessException(ResultCode.SMS_TEXT_IS_EMPTY, "请输入验证码");
        }
        SmsCodeCheckResDto res = verifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId(), dto.getSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_OLD_INVALID);
        }
        /**
         * 获取AES密钥信息
         */
        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + dto.getKid());
        stringRedisTemplate.delete(SECRET_INFO_KEY + dto.getKid());
        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.KID_EXPIRED_ERROR, "密钥ID已过期，请重新获取");
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        // 密码解密
        dto.setPassword(aes.getDecodeValue(dto.getPassword()));

        String password = dto.getPassword();

        // 判断密码不符合规则
        if (!password.matches(RegexConst.PASSWORD_REGEX) || ValidateUtil.checkRepeat(password)
                || ValidateUtil.checkBoardContinuousChar(password)) {
            throw new BusinessException(PASSWORD_IS_INVALID,"请设置8-20位字符，含数字、特殊字符（!@#$%^&*.?）、大小写字母的密码，且不能连续3位以上");
        }

        UserEntity userEntity = getUserById(dto.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        String oldPhone = Optional.ofNullable(userEntity).map(d -> d.getPhoneNumber()).orElse(null);
        if (!dto.getPhoneNumber().equals(oldPhone)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "手机号不一致");
        }
        //修改密码
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(dto.getUserId());
        updateEntity.setPassword(new Password(password).getHashEncryptValue());
        updateEntity.setUpdatedOn(LocalDateTime.now());
        userEntityMapper.updateById(updateEntity);
    }
}
