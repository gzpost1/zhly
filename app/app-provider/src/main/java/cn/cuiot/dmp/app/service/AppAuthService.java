package cn.cuiot.dmp.app.service;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_OLD_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_LOCKED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR;

import cn.cuiot.dmp.app.converter.AppUserConverter;
import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.user.ChangePhoneDto;
import cn.cuiot.dmp.app.dto.user.PhoneLoginDto;
import cn.cuiot.dmp.app.dto.user.PwdChangeDto;
import cn.cuiot.dmp.app.dto.user.PwdLoginDto;
import cn.cuiot.dmp.app.dto.user.PwdResetDto;
import cn.cuiot.dmp.app.dto.user.SampleUserInfoDto;
import cn.cuiot.dmp.app.dto.user.SmsCodeCheckResDto;
import cn.cuiot.dmp.app.dto.user.SwitchUserTypeDto;
import cn.cuiot.dmp.app.entity.OrganizationEntity;
import cn.cuiot.dmp.app.entity.UserEntity;
import cn.cuiot.dmp.app.mapper.OrganizationEntityMapper;
import cn.cuiot.dmp.app.util.RandomPwUtils;
import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonMenuDto;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserStatusEnum;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * APP登录服务
 *
 * @author: wuyongchong
 * @date: 2024/5/22 15:05
 */
@Slf4j
@Service
public class AppAuthService {

    @Autowired
    private AppVerifyService appVerifyService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserConverter appUserConverter;

    @Autowired
    private RandomPwUtils randomPwUtils;

    @Autowired
    private OrganizationEntityMapper organizationEntityMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ApiSystemService apiSystemService;

    /**
     * 构建密钥内容
     */
    private Map<String, Object> generateClaims(AppUserDto userDto) {
        Map<String, Object> claims = Maps.newHashMap();

        claims.put(AuthContants.USERORG, userDto.getOrgId());
        claims.put(AuthContants.USERORG_TYPE_ID, userDto.getOrgTypeId());
        claims.put(AuthContants.DEPT_ID, userDto.getDeptId());
        claims.put(AuthContants.POST_ID, userDto.getPostId());

        claims.put(AuthContants.CREATED, new Date());
        claims.put(AuthContants.USERID, userDto.getId());
        claims.put(AuthContants.USERNAME, userDto.getUsername());
        claims.put(AuthContants.USER_PHONE, userDto.getPhoneNumber());
        claims.put(AuthContants.NAME, userDto.getName());
        claims.put(AuthContants.USER_TYPE, userDto.getUserType());

        return claims;
    }

    /**
     * openid登录
     */
    public List<AppUserDto> openidLogin(String openid, String ipAddr) {
        List<AppUserDto> resultList = Lists.newArrayList();
        List<AppUserDto> appUserDtos = appUserService.selectUserByOpenid(openid);
        if (CollectionUtils.isNotEmpty(appUserDtos)) {
            for (AppUserDto userDto : appUserDtos) {

                if (UserTypeEnum.USER.getValue().equals(userDto.getUserType())) {
                    if (!EntityConstants.DISABLED.equals(userDto.getStatus())) {
                        Long pkOrgId = appUserService.getOrgId(userDto.getId());
                        if (Objects.nonNull(pkOrgId)) {
                            OrganizationEntity organization = organizationEntityMapper
                                    .selectById(pkOrgId);
                            if (Objects.nonNull(organization) && !OrgStatusEnum.DISABLE.getCode()
                                    .equals(organization.getStatus())) {
                                String pkDeptId = appUserService
                                        .getDeptId(userDto.getId().toString(), pkOrgId.toString());
                                userDto.setOrgId(pkOrgId.toString());
                                userDto.setDeptId(pkDeptId);
                                userDto.setOrgTypeId(organization.getOrgTypeId());
                                //更新登录时间
                                UserEntity updateEntity = new UserEntity();
                                updateEntity.setId(userDto.getId());
                                updateEntity.setOpenid(openid);
                                updateEntity.setLastOnlineIp(ipAddr);
                                updateEntity.setLastOnlineOn(LocalDateTime.now());
                                appUserService.updateAppUser(updateEntity);

                                if(StringUtils.isNotBlank(openid)){
                                    userDto.setOpenid(openid);
                                }
                                //设置权限
                                setPermissionMenus(userDto);
                                //设置token
                                genSetAppToken(userDto);

                                resultList.add(userDto);
                            }
                        }
                    }
                } else {
                    //更新登录时间
                    UserEntity updateEntity = new UserEntity();
                    updateEntity.setId(userDto.getId());
                    updateEntity.setOpenid(openid);
                    updateEntity.setLastOnlineIp(ipAddr);
                    updateEntity.setLastOnlineOn(LocalDateTime.now());
                    appUserService.updateAppUser(updateEntity);

                    if(StringUtils.isNotBlank(openid)){
                        userDto.setOpenid(openid);
                    }
                    //设置token
                    genSetAppToken(userDto);

                    resultList.add(userDto);
                }

            }
        }
        return resultList;
    }

    /**
     * 设置菜单权限
     * @param userDto
     */
    private void setPermissionMenus(AppUserDto userDto){
        List<CommonMenuDto> menuDtoList = apiSystemService
                .getPermissionMenus(userDto.getOrgId(), userDto.getId().toString());
        userDto.setMenu(menuDtoList);
        userDto.setPermission_ids(Lists.newArrayList());
        if(CollectionUtils.isNotEmpty(menuDtoList)){
            userDto.setPermission_ids(menuDtoList.stream()
                    .filter(ite-> org.apache.commons.lang3.StringUtils.isNotBlank(ite.getPermissionCode()))
                    .map(ite->ite.getPermissionCode())
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 设置token
     */
    private void genSetAppToken(AppUserDto userDto) {
        Long pkUserId = userDto.getId();
        Date expirationDate = DateUtil
                .date(System.currentTimeMillis() + Const.WX_SESSION_TIME * 1000);

        Map<String, Object> claims = generateClaims(userDto);

        String jwt = Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Const.SECRET).compact();

        String refreshCode = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                SecurityConst.WX_REFRESH_SESSION_TIME);
        redisUtil.set(CacheConst.LOGIN_USERS_JWT_WX + jwt, String.valueOf(pkUserId),
                Const.WX_SESSION_TIME);

        userDto.setRefreshCode(refreshCode);
        userDto.setToken(jwt);
    }

    /**
     * 小程序授权登录
     */
    public AppUserDto miniLogin(String phone, Integer userType, String openid, String ipAddr) {
        //获得用户信息
        AppUserDto userDto = appUserService.getUserByPhoneAndUserType(phone, userType);
        //员工
        if (UserTypeEnum.USER.getValue().equals(userType)) {
            if (Objects.isNull(userDto)) {
                throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST, "用户不存在");
            }
            if (EntityConstants.DISABLED.equals(userDto.getStatus())) {
                throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "用户已被禁用");
            }
            Long pkUserId = userDto.getId();
            // 获取org主键id
            Long pkOrgId = appUserService.getOrgId(pkUserId);
            // 获取dept主键id
            String pkDeptId = appUserService.getDeptId(pkUserId.toString(), pkOrgId.toString());
            // 获取企业信息
            OrganizationEntity organization = organizationEntityMapper.selectById(pkOrgId);
            if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                    .getCode().equals(organization.getStatus())) {
                throw new BusinessException(ResultCode.ORG_IS_ENABLED);
            }
            userDto.setOrgId(pkOrgId.toString());
            userDto.setDeptId(pkDeptId);
            userDto.setOrgTypeId(organization.getOrgTypeId());

            //设置权限
            setPermissionMenus(userDto);

            //更新登录时间
            UserEntity updateEntity = new UserEntity();
            updateEntity.setId(userDto.getId());
            updateEntity.setOpenid(openid);
            updateEntity.setLastOnlineIp(ipAddr);
            updateEntity.setLastOnlineOn(LocalDateTime.now());
            appUserService.updateAppUser(updateEntity);
        } else {
            //非员工
            if (Objects.isNull(userDto)) {
                String password = randomPwUtils
                        .getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
                UserEntity userEntity = new UserEntity();
                userEntity.setName("用户昵称");
                userEntity.setUsername(phone + userType);
                userEntity.setPassword(new Password(password).getHashEncryptValue());
                userEntity.setPhoneNumber(new PhoneNumber(phone).getEncryptedValue());
                userEntity.setStatus(UserStatusEnum.OPEN.getValue());
                userEntity.setUserType(userType);
                userEntity.setOpenid(openid);
                userEntity.setDeletedFlag(EntityConstants.NOT_DELETED.intValue());
                userEntity.setLastOnlineIp(ipAddr);
                userEntity.setLastOnlineOn(LocalDateTime.now());
                userEntity.setCreatedOn(LocalDateTime.now());
                userEntity.setCreatedBy(OperateByTypeEnum.SYSTEM.name().toLowerCase());
                userEntity.setCreatedByType(OperateByTypeEnum.SYSTEM.getValue());
                appUserService.createAppUser(userEntity);
                userDto = appUserConverter.toAppUserDto(userEntity);
            } else {
                //更新登录时间
                UserEntity updateEntity = new UserEntity();
                updateEntity.setId(userDto.getId());
                updateEntity.setOpenid(openid);
                updateEntity.setLastOnlineIp(ipAddr);
                updateEntity.setLastOnlineOn(LocalDateTime.now());
                appUserService.updateAppUser(updateEntity);
            }
        }
        if(StringUtils.isNotBlank(openid)){
            userDto.setOpenid(openid);
        }
        //生成与设置token
        genSetAppToken(userDto);

        return userDto;
    }

    /**
     * 密码登录
     */
    public AppUserDto pwdLogin(PwdLoginDto dto) {
        /**
         * 手机号校验
         */
        if (StringUtils.isBlank(dto.getUserAccount())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY, "请输入手机号");
        }
        if (!PhoneUtil.isPhone(dto.getUserAccount())) {
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
        // 验证码ID参数校验
        if (StringUtils.isBlank(dto.getSid())) {
            throw new BusinessException(ResultCode.ACCESS_ERROR, "验证码ID参数为空");
        }
        // 验证码参数校验
        if (StringUtils.isBlank(dto.getKaptchaText())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "请输入验证码");
        }
        //图形验证码校验
        if (!appVerifyService.checkKaptchaText(dto.getKaptchaText(), dto.getSid())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_ERROR, "验证码错误");
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

        String userAccount = dto.getUserAccount();
        Integer userType = dto.getUserType();
        String password = dto.getPassword();

        AppUserDto userDto = appUserService.getUserByPhoneAndUserType(userAccount, userType);

        // 账号不存在
        if (userDto == null || Objects.isNull(userDto.getStatus())) {
            //记录登录失败次数
            recordLoginFailedCount(userAccount);
        }

        // 账号被停用
        if (EntityConstants.DISABLED.equals(userDto.getStatus())) {
            recordLoginFailedCountWhenUserExist(userDto.getId());
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }

        // 密码验证
        if (!new Password().verifyPassword(userDto.getPassword(), password)) {
            //记录登录失败次数
            recordLoginFailedCountWhenUserExist(userDto.getId());
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }

        //登录成功-检验和清空登录失败次数
        checkAndClearLoginFailedCount(userDto.getId());

        //更新登录时间
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(userDto.getId());
        updateEntity.setLastOnlineIp(dto.getIpAddr());
        updateEntity.setLastOnlineOn(LocalDateTime.now());
        appUserService.updateAppUser(updateEntity);

        Long pkUserId = userDto.getId();
        // 获取org主键id
        Long pkOrgId = appUserService.getOrgId(pkUserId);
        // 获取dept主键id
        String pkDeptId = appUserService.getDeptId(pkUserId.toString(), pkOrgId.toString());
        // 获取企业信息
        OrganizationEntity organization = organizationEntityMapper.selectById(pkOrgId);
        if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                .getCode().equals(organization.getStatus())) {
            throw new BusinessException(ResultCode.ORG_IS_ENABLED);
        }
        userDto.setOrgId(pkOrgId.toString());
        userDto.setDeptId(pkDeptId);
        userDto.setOrgTypeId(organization.getOrgTypeId());

        //设置权限
        setPermissionMenus(userDto);

        //生成与设置token
        genSetAppToken(userDto);

        return userDto;
    }

    /**
     * 记录登录失败次数
     */
    private void recordLoginFailedCountWhenUserExist(Long userId) {
        String loginFailedUsersRedisKey =
                CacheConst.LOGIN_FAILED_USERS_REDIS_KEY + userId;
        // 登录失败次数自增
        long failedCounts = stringRedisTemplate.opsForValue().increment(loginFailedUsersRedisKey);
        // 设置过期时间
        stringRedisTemplate.expire(loginFailedUsersRedisKey, SecurityConst.LOGIN_FAILED_FREEZE_TIME,
                TimeUnit.MINUTES);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        } else {
            // 登录失败次数达到上限，账号冻结
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    /**
     * 记录登录失败次数
     */
    private void recordLoginFailedCount(String userAccount) {
        String redisKey =
                CacheConst.LOGIN_FAILED_NON_EXIST_USERS_REDIS_KEY + userAccount;
        // 登录失败次数自增
        long failedCounts = stringRedisTemplate.opsForValue().increment(redisKey);
        // 设置过期时间
        stringRedisTemplate
                .expire(redisKey, SecurityConst.LOGIN_FAILED_FREEZE_TIME, TimeUnit.MINUTES);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        } else {
            // 登录失败次数达到上限，账号冻结
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    /**
     * 校验账号是否已被冻结
     */
    private void checkLoginFailedCount(String userAccount) {
        String redisKey =
                CacheConst.LOGIN_FAILED_NON_EXIST_USERS_REDIS_KEY + userAccount;
        // 登录失败次数
        String failedCountsStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(failedCountsStr)) {
            long failedCounts = Long.valueOf(failedCountsStr);
            if (failedCounts > SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
                // 登录失败次数达到上限，账号已被冻结
                throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
            }
        }
    }

    /**
     * 检验和清空登录失败次数
     */
    private void checkAndClearLoginFailedCount(Long userId) {
        String loginFailedUsersRedisKey = CacheConst.LOGIN_FAILED_USERS_REDIS_KEY + userId;
        // 查询登录失败次数
        String failedCountsStr = stringRedisTemplate.opsForValue().get(loginFailedUsersRedisKey);
        Integer failedCounts = Optional.ofNullable(failedCountsStr)
                .map(fcs -> Integer.parseInt(fcs)).orElse(0);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            stringRedisTemplate.delete(loginFailedUsersRedisKey);
        } else {
            // 登录失败次数达到上限，账号冻结
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    /**
     * 手机号登录
     */
    public AppUserDto phoneLogin(PhoneLoginDto dto) {
        /**
         * 手机号校验
         */
        if (StringUtils.isBlank(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY, "请输入手机号");
        }
        if (!PhoneUtil.isPhone(dto.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入11位手机号码");
        }
        /**
         * 短信验证码参数校验
         */
        if (StringUtils.isBlank(dto.getSmsCode())) {
            throw new BusinessException(ResultCode.SMS_TEXT_IS_EMPTY, "请输入验证码");
        }
        SmsCodeCheckResDto res = appVerifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), null, dto.getSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_OLD_INVALID);
        }

        String userAccount = dto.getPhoneNumber();
        Integer userType = dto.getUserType();

        AppUserDto userDto = appUserService.getUserByPhoneAndUserType(userAccount, userType);

        //员工
        if (UserTypeEnum.USER.getValue().equals(userType)) {
            // 账号不存在
            if (userDto == null || Objects.isNull(userDto.getStatus())) {
                //记录登录失败次数
                recordLoginFailedCount(userAccount);
            }

            // 账号被停用
            if (EntityConstants.DISABLED.equals(userDto.getStatus())) {
                recordLoginFailedCountWhenUserExist(userDto.getId());
                throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
            }

            //登录成功-检验和清空登录失败次数
            checkAndClearLoginFailedCount(userDto.getId());

            Long pkUserId = userDto.getId();
            // 获取org主键id
            Long pkOrgId = appUserService.getOrgId(pkUserId);
            // 获取dept主键id
            String pkDeptId = appUserService.getDeptId(pkUserId.toString(), pkOrgId.toString());
            // 获取企业信息
            OrganizationEntity organization = organizationEntityMapper.selectById(pkOrgId);
            if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                    .getCode().equals(organization.getStatus())) {
                throw new BusinessException(ResultCode.ORG_IS_ENABLED);
            }
            userDto.setOrgId(pkOrgId.toString());
            userDto.setDeptId(pkDeptId);
            userDto.setOrgTypeId(organization.getOrgTypeId());

            //设置权限
            setPermissionMenus(userDto);

            //更新登录时间
            UserEntity updateEntity = new UserEntity();
            updateEntity.setId(userDto.getId());
            updateEntity.setLastOnlineIp(dto.getIpAddr());
            updateEntity.setLastOnlineOn(LocalDateTime.now());
            appUserService.updateAppUser(updateEntity);

        }else {
            //非员工
            if (Objects.isNull(userDto)) {
                String password = randomPwUtils
                        .getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
                UserEntity userEntity = new UserEntity();
                userEntity.setName("用户昵称");
                userEntity.setUsername(dto.getPhoneNumber() + userType);
                userEntity.setPassword(new Password(password).getHashEncryptValue());
                userEntity.setPhoneNumber(new PhoneNumber(dto.getPhoneNumber()).getEncryptedValue());
                userEntity.setStatus(UserStatusEnum.OPEN.getValue());
                userEntity.setUserType(userType);
                userEntity.setDeletedFlag(EntityConstants.NOT_DELETED.intValue());
                userEntity.setLastOnlineIp(dto.getIpAddr());
                userEntity.setLastOnlineOn(LocalDateTime.now());
                userEntity.setCreatedOn(LocalDateTime.now());
                userEntity.setCreatedBy(OperateByTypeEnum.SYSTEM.name().toLowerCase());
                userEntity.setCreatedByType(OperateByTypeEnum.SYSTEM.getValue());
                appUserService.createAppUser(userEntity);
                userDto = appUserConverter.toAppUserDto(userEntity);
            } else {
                //更新登录时间
                UserEntity updateEntity = new UserEntity();
                updateEntity.setId(userDto.getId());
                updateEntity.setLastOnlineIp(dto.getIpAddr());
                updateEntity.setLastOnlineOn(LocalDateTime.now());
                appUserService.updateAppUser(updateEntity);
            }
        }
        //生成与设置token
        genSetAppToken(userDto);

        return userDto;
    }

    /**
     * 用户登出
     */
    public void logOut(HttpServletRequest request) {
        try {
            String jwt = request.getHeader(AuthContants.TOKEN);
            redisUtil.del(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt);
            redisUtil.del(CacheConst.LOGIN_USERS_JWT_WX + jwt);
            redisUtil.del(CacheConst.LOGIN_USERS_JWT + jwt);
        } catch (Exception e) {
            log.error("logOut error", e);
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
    }

    /**
     * 设置用户头像与昵称
     */
    public void setSampleUserInfo(SampleUserInfoDto dto) {
        appUserService.setSampleUserInfo(dto.getUserId(), dto.getNickName(), dto.getAvatarUrl());
    }

    /**
     * 修改手机号
     */
    public void changePhone(ChangePhoneDto dto) {
        AppUserDto userDto = appUserService.getUserById(dto.getUserId());
        String oldPhone = Optional.ofNullable(userDto).map(d -> d.getPhoneNumber()).orElse(null);
        if (dto.getPhoneNumber().equals(oldPhone)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "新手机号不能与旧手机号一致");
        }
        SmsCodeCheckResDto res = appVerifyService
                .checkPhoneSmsCode(oldPhone, dto.getUserId(), dto.getPreSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_OLD_INVALID);
        }
        res = appVerifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId(), dto.getSmsCode(), true);
        if (!res.getCheckSucceed()) {
            throw new BusinessException(SMS_TEXT_ERROR);
        }
        appUserService.changePhone(dto.getUserId(), dto.getPhoneNumber());
    }

    /**
     * 获得登录用户信息
     */
    public AppUserDto getLoginUserInfo(Long sessionUserId) {
        AppUserDto userDto = appUserService.getUserById(sessionUserId);
        if (userDto == null) {
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST, "用户信息获取失败");
        }
        if (UserTypeEnum.USER.getValue().equals(userDto.getUserType())) {
            Long pkUserId = userDto.getId();
            // 获取org主键id
            Long pkOrgId = appUserService.getOrgId(pkUserId);
            // 获取dept主键id
            String pkDeptId = appUserService.getDeptId(pkUserId.toString(), pkOrgId.toString());
            // 获取企业信息
            OrganizationEntity organization = organizationEntityMapper.selectById(pkOrgId);
            if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                    .getCode().equals(organization.getStatus())) {
                throw new BusinessException(ResultCode.ORG_IS_ENABLED);
            }
            userDto.setOrgId(pkOrgId.toString());
            userDto.setDeptId(pkDeptId);
            userDto.setOrgTypeId(organization.getOrgTypeId());

            //设置权限
            setPermissionMenus(userDto);
        }
        return userDto;
    }

    /**
     * 密码重置
     */
    public void resetPwd(PwdResetDto dto) {
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
        SmsCodeCheckResDto res = appVerifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), null, dto.getSmsCode(), true);
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

        String userAccount = dto.getPhoneNumber();
        Integer userType = dto.getUserType();
        String password = dto.getPassword();

        AppUserDto userDto = appUserService.getUserByPhoneAndUserType(userAccount, userType);
        if (Objects.isNull(userDto)) {
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        //修改密码
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(userDto.getId());
        updateEntity.setPassword(new Password(password).getHashEncryptValue());
        updateEntity.setUpdatedOn(LocalDateTime.now());
        appUserService.updateAppUser(updateEntity);
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
        SmsCodeCheckResDto res = appVerifyService
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

        AppUserDto userDto = appUserService.getUserById(dto.getUserId());
        if (Objects.isNull(userDto)) {
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        String oldPhone = Optional.ofNullable(userDto).map(d -> d.getPhoneNumber()).orElse(null);
        if (!dto.getPhoneNumber().equals(oldPhone)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "手机号不一致");
        }
        //修改密码
        UserEntity updateEntity = new UserEntity();
        updateEntity.setId(userDto.getId());
        updateEntity.setPassword(new Password(password).getHashEncryptValue());
        updateEntity.setUpdatedOn(LocalDateTime.now());
        appUserService.updateAppUser(updateEntity);
    }

    /**
     * 切换身份
     */
    public AppUserDto switchUserType(SwitchUserTypeDto dto) {
        AppUserDto currentUser = appUserService.getUserById(dto.getUserId());
        if (Objects.isNull(currentUser)) {
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        String ipAddr = dto.getIpAddr();
        String openid = dto.getOpenid();
        Integer userType = dto.getUserType();
        AppUserDto userDto = null;
        //员工
        if (UserTypeEnum.USER.getValue().equals(userType)) {
            //appUserService.getUserByOpenid(openid, userType)
            userDto = appUserService
                    .getUserByPhoneAndUserType(currentUser.getPhoneNumber(), userType);
            if (Objects.isNull(userDto)) {
                throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST, "用户不存在");
            }
            if (EntityConstants.DISABLED.equals(userDto.getStatus())) {
                throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID, "用户已被禁用");
            }
            Long pkUserId = userDto.getId();
            // 获取org主键id
            Long pkOrgId = appUserService.getOrgId(pkUserId);
            // 获取dept主键id
            String pkDeptId = appUserService.getDeptId(pkUserId.toString(), pkOrgId.toString());
            // 获取企业信息
            OrganizationEntity organization = organizationEntityMapper.selectById(pkOrgId);
            if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                    .getCode().equals(organization.getStatus())) {
                throw new BusinessException(ResultCode.ORG_IS_ENABLED);
            }
            userDto.setOrgId(pkOrgId.toString());
            userDto.setDeptId(pkDeptId);
            userDto.setOrgTypeId(organization.getOrgTypeId());

            //设置权限
            setPermissionMenus(userDto);

            //更新登录时间
            UserEntity updateEntity = new UserEntity();
            updateEntity.setId(userDto.getId());
            if(StringUtils.isBlank(userDto.getOpenid())) {
                updateEntity.setOpenid(openid);
            }
            updateEntity.setLastOnlineIp(ipAddr);
            updateEntity.setLastOnlineOn(LocalDateTime.now());
            appUserService.updateAppUser(updateEntity);
        } else {
            userDto = appUserService
                    .getUserByPhoneAndUserType(currentUser.getPhoneNumber(), userType);
            //非员工
            if (Objects.isNull(userDto)) {
                /*String password = randomPwUtils
                        .getRandomPassword((int) (8 + Math.random() * (20 - 8 + 1)));
                UserEntity userEntity = new UserEntity();
                userEntity.setName("用户昵称");
                userEntity.setUsername(userDto.getPhoneNumber() + userType);
                userEntity.setPassword(new Password(password).getHashEncryptValue());
                userEntity.setPhoneNumber(
                        new PhoneNumber(userDto.getPhoneNumber()).getEncryptedValue());
                userEntity.setStatus(UserStatusEnum.OPEN.getValue());
                userEntity.setUserType(userType);
                userEntity.setOpenid(openid);
                userEntity.setDeletedFlag(EntityConstants.NOT_DELETED.intValue());
                userEntity.setLastOnlineIp(ipAddr);
                userEntity.setLastOnlineOn(LocalDateTime.now());
                userEntity.setCreatedOn(LocalDateTime.now());
                userEntity.setCreatedBy(OperateByTypeEnum.SYSTEM.name().toLowerCase());
                userEntity.setCreatedByType(OperateByTypeEnum.SYSTEM.getValue());
                appUserService.createAppUser(userEntity);
                userDto = appUserConverter.toAppUserDto(userEntity);*/
                throw new BusinessException(ResultCode.USER_ACCOUNT_NOT_EXIST, "用户不存在");
            } else {
                //更新登录时间
                UserEntity updateEntity = new UserEntity();
                updateEntity.setId(userDto.getId());
                if(StringUtils.isBlank(userDto.getOpenid())) {
                    updateEntity.setOpenid(openid);
                }
                updateEntity.setLastOnlineIp(ipAddr);
                updateEntity.setLastOnlineOn(LocalDateTime.now());
                appUserService.updateAppUser(updateEntity);
            }
        }
        if(StringUtils.isBlank(userDto.getOpenid())) {
            userDto.setOpenid(openid);
        }
        //生成与设置token
        genSetAppToken(userDto);

        return userDto;
    }

}

