package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.ResultCode.INNER_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_LOCKED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR;

import cn.cuiot.dmp.base.application.enums.DeletedFlagEnum;
import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.UserLongTimeLoginEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.system.application.param.command.UpdateUserCommand;
import cn.cuiot.dmp.system.application.service.LoginService;
import cn.cuiot.dmp.system.application.service.OperateLogService;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrganizationResDTO;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * @author wensq
 * @version 1.0
 * @date 2022/9/16 10:41
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OperateLogService operateLogService;


    @Override
    public User authDmp(LoginReqDTO loginReqDTO) {
        // 获取用户账号，可能是用户名或手机号或邮箱
        String userAccount = loginReqDTO.getUserAccount();
        // 加密后的用户名或邮箱或者手机号
        Email email = null;
        PhoneNumber phoneNumber = null;
        if (Validator.isEmail(userAccount, true)) {
            email = new Email(userAccount);
        }
        if (PhoneUtil.isPhone(userAccount)) {
            phoneNumber = new PhoneNumber(userAccount);
        }
        // 查询用户
        User userEntity = userRepository
                .queryByUserNameOrPhoneNumberOrEmail(userAccount, phoneNumber, email);
        // 账号不存在
        if (userEntity == null) {
            //记录登录失败次数
            recordLoginFailedCount(userAccount);
        }
        // 账号存在
        String loginFailedUsersRedisKey =
                CacheConst.LOGIN_FAILED_USERS_REDIS_KEY + userEntity.getId();
        // 登录失败
        if (!userEntity.getPassword().verifyPassword(loginReqDTO.getPassword())) {
            //记录登录失败次数
            recordLoginFailedCountWhenUserExist(loginFailedUsersRedisKey);
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
        // 登录成功
        else {
            //清空登录失败次数
            clearLoginFailedCount(loginFailedUsersRedisKey);
            // 更新最后上线时间
            userEntity.setLastOnlineOn(LocalDateTime.now());
            boolean success = userRepository.save(userEntity);
            if (!success) {
                throw new BusinessException(INNER_ERROR);
            }
            return userEntity;
        }
    }

    private void clearLoginFailedCount(String loginFailedUsersRedisKey) {
        // 查询登录失败次数
        String failedCountsStr = stringRedisTemplate.opsForValue().get(loginFailedUsersRedisKey);
        Integer failedCounts = Optional.ofNullable(failedCountsStr)
                .map(fcs -> Integer.parseInt(fcs)).orElse(0);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            stringRedisTemplate.delete(loginFailedUsersRedisKey);
        }
        // 登录失败次数达到上限，账号冻结
        else {
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    private void recordLoginFailedCountWhenUserExist(String loginFailedUsersRedisKey) {
        // 登录失败次数自增
        long failedCounts = stringRedisTemplate.opsForValue().increment(loginFailedUsersRedisKey);
        // 设置过期时间
        stringRedisTemplate.expire(loginFailedUsersRedisKey, SecurityConst.LOGIN_FAILED_FREEZE_TIME,
                TimeUnit.MINUTES);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
        // 登录失败次数达到上限，账号冻结
        else {
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    private void recordLoginFailedCount(String userAccount) {
        String loginFailedNonExistUsersRedisKey =
                CacheConst.LOGIN_FAILED_NON_EXIST_USERS_REDIS_KEY + userAccount;
        // 登录失败次数自增
        long failedCounts = stringRedisTemplate.opsForValue()
                .increment(loginFailedNonExistUsersRedisKey);
        // 设置过期时间
        stringRedisTemplate
                .expire(loginFailedNonExistUsersRedisKey, SecurityConst.LOGIN_FAILED_FREEZE_TIME,
                        TimeUnit.MINUTES);
        // 登录失败次数未到上限
        if (failedCounts < SecurityConst.LOGIN_FAILED_MAX_COUNTS) {
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
        // 登录失败次数达到上限，账号冻结
        else {
            throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
        }
    }

    @Override
    public LoginResDTO loginIdentity(User validateUser, HttpServletRequest request) {

        // 登录日志数据
        OperateLogDto platformOperateLogDTO = new OperateLogDto();
        JSONObject requestParamJsonObject = new JSONObject(1);
        requestParamJsonObject.put("用户名", validateUser.getUsername());
        platformOperateLogDTO.setRequestParams(requestParamJsonObject.toJSONString());
        platformOperateLogDTO.setOperationCode("session");
        platformOperateLogDTO.setOperationName("登录");
        platformOperateLogDTO.setOperationTarget(validateUser.getUsername());
        platformOperateLogDTO.setServiceType(ServiceTypeConst.AUDIT_MANAGEMENT);

        // 获取用户自增id
        Long pkUserId = Optional.ofNullable(validateUser).map(u -> u.getId().getValue())
                .orElse(null);

        boolean isSuccess = false;

        if (Objects.nonNull(pkUserId)) {
            // 用户请求ip
            String ipAddr = IpUtil.getIpAddr(request);
            // 脱敏手机号
            //String safePhoneNumber = (String) new StrategyPhone().des(validateUser.getDecryptedPhoneNumber(), null);
            String decryptedPhoneNumber = validateUser.getDecryptedPhoneNumber();
            // 获取org主键id
            String pkOrgId = userService.getOrgId(pkUserId);
            // 获取dept主键id
            String pkDeptId = userService.getDeptId(pkUserId.toString(), pkOrgId);
            // 获取账户信息
            OrganizationResDTO organization = organizationService.getOneById(pkOrgId);
            //校验账户状态
            checkOrgStatus(validateUser, organization);

            Date expirationDate = DateUtil
                    .date(System.currentTimeMillis() + Const.SESSION_TIME * 20 * 1000);
            Map<String, Object> claims = new HashMap<>(5);
            claims.put(AuthContants.USERNAME, validateUser.getUsername());
            claims.put(AuthContants.CREATED, new Date());
            claims.put(AuthContants.USERORG, pkOrgId);
            claims.put(AuthContants.USERORG_TYPE_ID, organization.getOrgTypeId());
            claims.put(AuthContants.USERID, pkUserId);
            claims.put(AuthContants.USER_PHONE, decryptedPhoneNumber);
            claims.put(AuthContants.NAME, validateUser.getName());
            claims.put(AuthContants.DEPT_ID, pkDeptId);
            claims.put(AuthContants.POST_ID, validateUser.getPostId());
            claims.put(AuthContants.USER_TYPE, validateUser.getUserType().getValue());

            String jwt = Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, Const.SECRET).compact();

            String refreshCode = String.valueOf(SnowflakeIdWorkerUtil.nextId());
            if (Objects.equals(validateUser.getLongTimeLogin(),
                    UserLongTimeLoginEnum.OPEN.getCode())) {
                redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                        SecurityConst.REFRESH_SESSION_TIME * 18);
                // 登陆要求不互踢
                redisUtil.set(CacheConst.LOGIN_USERS_JWT + jwt, String.valueOf(pkUserId),
                        Const.USER_LONG_TIME_LOGIN_SESSION_TIME);
                // 设置缓存，表示该用户为长期登录，方便gateway识别是否为长期登录用户，从而token续期
                redisUtil.set(CacheConst.USER_LONG_TIME_LOGIN + pkUserId, Const.STR_1,
                        Const.USER_LONG_TIME_LOGIN_SESSION_TIME);
            } else {
                redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                        SecurityConst.REFRESH_SESSION_TIME);
                // 登陆要求不互踢
                redisUtil.set(CacheConst.LOGIN_USERS_JWT + jwt, String.valueOf(pkUserId),
                        Const.SESSION_TIME);
            }

            //保存登录成功日志
            isSuccess = true;
            platformOperateLogDTO.setOrgId(pkOrgId);
            platformOperateLogDTO.setOperationById(String.valueOf(pkUserId));
            platformOperateLogDTO.setOperationByName(validateUser.getUsername());
            saveLog2Db(isSuccess, platformOperateLogDTO, request);

            //redis记录当前登录账号
            List<String> orgInfoList = redisUtil
                    .lGet(CacheConst.USER_ORG_CURRENT_KEY + pkUserId, 0, -1);
            // 如果集合中存在该值,删除之前的记录
            for (String orgInfo : orgInfoList) {
                if (orgInfo.equals(pkOrgId)) {
                    redisUtil.lRemove(CacheConst.USER_ORG_CURRENT_KEY + pkUserId,
                            SecurityConst.USER_ORG_LOGIN_RECORD_MAX, orgInfo);
                }
            }
            // 限制redis存储长度
            Long size = redisUtil.lGetListSize(CacheConst.USER_ORG_CURRENT_KEY + pkUserId);
            if (size >= SecurityConst.USER_ORG_LOGIN_RECORD_MAX) {
                redisUtil.lPopForList(CacheConst.USER_ORG_CURRENT_KEY + pkUserId);
            }
            redisUtil.lSet(CacheConst.USER_ORG_CURRENT_KEY + pkUserId, pkOrgId);

            // 更新最近登录ip和城市
            UpdateUserCommand updateUserParams = new UpdateUserCommand();
            updateUserParams.setId(validateUser.getId().getValue());
            updateUserParams.setLastOnlineIp(ipAddr);
            userService.updateByCommand(updateUserParams);

            // 初始化返回对象
            LoginResDTO loginResDTO = new LoginResDTO();
            loginResDTO.setPhoneNumber(decryptedPhoneNumber);
            loginResDTO.setName(validateUser.getName());
            loginResDTO.setRefreshCode(refreshCode);
            loginResDTO.setToken(jwt);
            loginResDTO.setUserId(String.valueOf(pkUserId));
            loginResDTO.setOrgId(pkOrgId);
            loginResDTO.setOrgName(organization.getOrgName());
            loginResDTO.setOrgTypeId(organization.getOrgTypeId());
            loginResDTO.setPostId(validateUser.getPostId());
            loginResDTO.setAvatar(validateUser.getAvatar());
            loginResDTO.setUserType(validateUser.getUserType().getValue());

            return loginResDTO;

        } else {
            this.saveLog2Db(isSuccess, platformOperateLogDTO, request);
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
    }

    private static void checkOrgStatus(User validateUser, OrganizationResDTO organization) {
        // 查看账户状态
        if (organization == null || organization.getStatus() == null || OrgStatusEnum.DISABLE
                .getCode().equals(organization.getStatus())) {
            throw new BusinessException(ResultCode.ORG_IS_ENABLED);
        }
        if (DeletedFlagEnum.DELETED.getCode().equals(organization.getDeletedFlag())
                || DeletedFlagEnum.DELETED.getCode().equals(validateUser.getDeletedFlag())) {
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR);
        }
    }

    @Override
    public void logoutIdentity(HttpServletRequest request, String orgId, String userId,
            String userName) {
        boolean isSuccess = false;
        // 填充日志数据
        OperateLogDto platformOperateLogDTO = new OperateLogDto();
        platformOperateLogDTO.setOrgId(orgId);
        platformOperateLogDTO.setOperationById(userId);
        platformOperateLogDTO.setOperationByName(userName);
        platformOperateLogDTO.setOperationTarget(userName);
        platformOperateLogDTO.setOperationCode("session");
        platformOperateLogDTO.setOperationName("注销");
        platformOperateLogDTO.setServiceType(ServiceTypeConst.AUDIT_MANAGEMENT);
        try {
            String jwt = request.getHeader(AuthContants.TOKEN);
            redisUtil.del(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt);
            redisUtil.del(CacheConst.LOGIN_USERS_JWT + jwt);

            // 注销成功，发送日志
            // 发送日志至kafka
            isSuccess = true;
            this.saveLog2Db(isSuccess, platformOperateLogDTO, request);
        } catch (Exception e) {
            // 注销失败发送日志
            // 发送日志至kafka
            this.saveLog2Db(isSuccess, platformOperateLogDTO, request);

            throw e;
        }
    }

    /**
     * 发送日志记录到kafka
     *
     * @param isSuccess 操作成功标识
     * @param platformOperateLogDTO 日志记录
     */
    public void saveLog2Db(boolean isSuccess, OperateLogDto platformOperateLogDTO,
            HttpServletRequest request) {
        platformOperateLogDTO.setRequestTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        platformOperateLogDTO.setRequestIp(IpUtil.getIpAddr(request));
        if (isSuccess) {
            // 操作成功
            platformOperateLogDTO
                    .setStatusCode(cn.cuiot.dmp.common.enums.StatusCodeEnum.SUCCESS.getCode());
            platformOperateLogDTO
                    .setStatusMsg(cn.cuiot.dmp.common.enums.StatusCodeEnum.SUCCESS.getName());
            platformOperateLogDTO
                    .setLogLevel(cn.cuiot.dmp.common.enums.LogLevelEnum.INFO.getCode());
        } else {
            platformOperateLogDTO
                    .setLogLevel(cn.cuiot.dmp.common.enums.LogLevelEnum.ERROR.getCode());
            platformOperateLogDTO
                    .setStatusMsg(cn.cuiot.dmp.common.enums.StatusCodeEnum.FAILED.getName());
            platformOperateLogDTO
                    .setStatusCode(cn.cuiot.dmp.common.enums.StatusCodeEnum.FAILED.getCode());
        }

        operateLogService.saveDb(platformOperateLogDTO);
    }


    /**
     * 模拟登录
     */
    @Override
    public LoginResDTO simulateLogin(String username, String phone, HttpServletRequest request) {
        /**
         * 查询用户
         */
        PhoneNumber phoneNumber = null;
        if (PhoneUtil.isPhone(username)) {
            phoneNumber = new PhoneNumber(username);
        }
        if (StringUtils.isNotBlank(phone)) {
            phoneNumber = new PhoneNumber(username);
        }
        User validateUser = userRepository
                .queryByUserNameOrPhoneNumberOrEmail(username, phoneNumber, null);
        // 获取用户自增id
        Long pkUserId = Optional.ofNullable(validateUser).map(u -> u.getId().getValue())
                .orElse(null);

        // 用户请求ip
        String ipAddr = IpUtil.getIpAddr(request);
        // 脱敏手机号
        String decryptedPhoneNumber = validateUser.getDecryptedPhoneNumber();
        // 获取org主键id
        String pkOrgId = userService.getOrgId(pkUserId);
        // 获取dept主键id
        String pkDeptId = userService.getDeptId(pkUserId.toString(), pkOrgId);
        // 获取账户信息
        OrganizationResDTO organization = organizationService.getOneById(pkOrgId);
        //校验账户状态
        checkOrgStatus(validateUser, organization);

        Date expirationDate = DateUtil
                .date(System.currentTimeMillis() + Const.SESSION_TIME * 20 * 1000);
        Map<String, Object> claims = new HashMap<>(5);
        claims.put(AuthContants.USERNAME, validateUser.getUsername());
        claims.put(AuthContants.CREATED, new Date());
        claims.put(AuthContants.USERORG, pkOrgId);
        claims.put(AuthContants.USERORG_TYPE_ID, organization.getOrgTypeId());
        claims.put(AuthContants.USERID, pkUserId);
        claims.put(AuthContants.USER_PHONE, decryptedPhoneNumber);
        claims.put(AuthContants.NAME, validateUser.getName());
        claims.put(AuthContants.DEPT_ID, pkDeptId);
        claims.put(AuthContants.POST_ID, validateUser.getPostId());
        claims.put(AuthContants.USER_TYPE, validateUser.getUserType().getValue());

        String jwt = Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Const.SECRET).compact();

        String refreshCode = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        if (Objects.equals(validateUser.getLongTimeLogin(), UserLongTimeLoginEnum.OPEN.getCode())) {
            redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                    SecurityConst.REFRESH_SESSION_TIME * 18);
            // 登陆要求不互踢
            redisUtil.set(CacheConst.LOGIN_USERS_JWT + jwt, String.valueOf(pkUserId),
                    Const.USER_LONG_TIME_LOGIN_SESSION_TIME);
            // 设置缓存，表示该用户为长期登录，方便gateway识别是否为长期登录用户，从而token续期
            redisUtil.set(CacheConst.USER_LONG_TIME_LOGIN + pkUserId, Const.STR_1,
                    Const.USER_LONG_TIME_LOGIN_SESSION_TIME);
        } else {
            redisUtil.set(CacheConst.LOGIN_USERS_REFRESH_CODE + jwt, refreshCode,
                    SecurityConst.REFRESH_SESSION_TIME);
            // 登陆要求不互踢
            redisUtil.set(CacheConst.LOGIN_USERS_JWT + jwt, String.valueOf(pkUserId),
                    Const.SESSION_TIME);
        }

        // 登录日志数据
        OperateLogDto platformOperateLogDTO = new OperateLogDto();
        JSONObject requestParamJsonObject = new JSONObject(1);
        requestParamJsonObject.put("用户名", validateUser.getUsername());
        platformOperateLogDTO.setRequestParams(requestParamJsonObject.toJSONString());
        platformOperateLogDTO.setOperationCode("session");
        platformOperateLogDTO.setOperationName("模拟登录企业");
        platformOperateLogDTO.setOperationTarget(validateUser.getUsername());
        platformOperateLogDTO.setServiceType(ServiceTypeConst.AUDIT_MANAGEMENT);
        platformOperateLogDTO.setOrgId(pkOrgId);
        platformOperateLogDTO.setOperationById(String.valueOf(LoginInfoHolder.getCurrentUserId()));
        platformOperateLogDTO.setOperationByName(LoginInfoHolder.getCurrentUsername());
        saveLog2Db(true, platformOperateLogDTO, request);

        //redis记录当前登录账号
        List<String> orgInfoList = redisUtil
                .lGet(CacheConst.USER_ORG_CURRENT_KEY + pkUserId, 0, -1);
        // 如果集合中存在该值,删除之前的记录
        for (String orgInfo : orgInfoList) {
            if (orgInfo.equals(pkOrgId)) {
                redisUtil.lRemove(CacheConst.USER_ORG_CURRENT_KEY + pkUserId,
                        SecurityConst.USER_ORG_LOGIN_RECORD_MAX, orgInfo);
            }
        }
        // 限制redis存储长度
        Long size = redisUtil.lGetListSize(CacheConst.USER_ORG_CURRENT_KEY + pkUserId);
        if (size >= SecurityConst.USER_ORG_LOGIN_RECORD_MAX) {
            redisUtil.lPopForList(CacheConst.USER_ORG_CURRENT_KEY + pkUserId);
        }
        redisUtil.lSet(CacheConst.USER_ORG_CURRENT_KEY + pkUserId, pkOrgId);

        // 更新最近登录ip和城市
        UpdateUserCommand updateUserParams = new UpdateUserCommand();
        updateUserParams.setId(validateUser.getId().getValue());
        updateUserParams.setLastOnlineIp(ipAddr);
        userService.updateByCommand(updateUserParams);

        // 初始化返回对象
        LoginResDTO loginResDTO = new LoginResDTO();
        loginResDTO.setPhoneNumber(decryptedPhoneNumber);
        loginResDTO.setName(validateUser.getName());
        loginResDTO.setRefreshCode(refreshCode);
        loginResDTO.setToken(jwt);
        loginResDTO.setUserId(String.valueOf(pkUserId));
        loginResDTO.setOrgId(pkOrgId);
        loginResDTO.setOrgName(organization.getOrgName());
        loginResDTO.setOrgTypeId(organization.getOrgTypeId());
        loginResDTO.setPostId(validateUser.getPostId());
        loginResDTO.setAvatar(validateUser.getAvatar());
        loginResDTO.setUserType(validateUser.getUserType().getValue());

        return loginResDTO;
    }
}
