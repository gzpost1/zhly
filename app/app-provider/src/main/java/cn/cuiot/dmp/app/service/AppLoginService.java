package cn.cuiot.dmp.app.service;

import cn.cuiot.dmp.app.converter.AppUserConverter;
import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.login.SampleUserInfoDto;
import cn.cuiot.dmp.app.entity.OrganizationEntity;
import cn.cuiot.dmp.app.entity.UserEntity;
import cn.cuiot.dmp.app.mapper.OrganizationEntityMapper;
import cn.cuiot.dmp.app.util.RandomPwUtils;
import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserStatusEnum;
import cn.cuiot.dmp.system.domain.types.enums.UserTypeEnum;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * APP登录服务
 *
 * @author: wuyongchong
 * @date: 2024/5/22 15:05
 */
@Slf4j
@Service
public class AppLoginService {

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
     * 小程序授权登录
     */
    public AppUserDto miniLogin(String phone, Integer userType, String openid, String ipAddr) {
        Map<String, Object> claims = Maps.newHashMap();
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

            claims.put(AuthContants.USERORG, pkOrgId);
            claims.put(AuthContants.USERORG_TYPE_ID, organization.getOrgTypeId());
            claims.put(AuthContants.DEPT_ID, pkDeptId);
            claims.put(AuthContants.POST_ID, userDto.getPostId());
            //更新登录时间
            UserEntity updateEntity = new UserEntity();
            updateEntity.setId(userDto.getId());
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
            }else{
                //更新登录时间
                UserEntity updateEntity = new UserEntity();
                updateEntity.setId(userDto.getId());
                updateEntity.setLastOnlineIp(ipAddr);
                updateEntity.setLastOnlineOn(LocalDateTime.now());
                appUserService.updateAppUser(updateEntity);
            }
        }
        Long pkUserId = userDto.getId();
        Date expirationDate = DateUtil.date(System.currentTimeMillis() + Const.WX_SESSION_TIME * 1000);
        claims.put(AuthContants.CREATED, new Date());
        claims.put(AuthContants.USERID, userDto.getId());
        claims.put(AuthContants.USERNAME, userDto.getUsername());
        claims.put(AuthContants.USER_PHONE, userDto.getPhoneNumber());
        claims.put(AuthContants.NAME, userDto.getName());
        claims.put(AuthContants.USER_TYPE, userDto.getUserType());

        String jwt = Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Const.SECRET).compact();

        String refreshCode = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                SecurityConst.WX_REFRESH_SESSION_TIME);
        redisUtil.set(CacheConst.LOGIN_USERS_JWT_WX + jwt, String.valueOf(pkUserId),
                Const.WX_SESSION_TIME);

        userDto.setRefreshCode(refreshCode);
        userDto.setToken(jwt);

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
            log.error("logOut error",e);
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
    }

    /**
     * 设置用户头像与昵称
     */
    public void setUserInfo(SampleUserInfoDto dto) {
        appUserService.setUserInfo(dto.getUserId(),dto.getNickName(),dto.getAvatarUrl());
    }

}
