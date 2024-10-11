package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.application.service.*;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsReqDTO;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR;


/**
 * 登录登出管理
 *
 * @author guoying
 * @className LogControllerImpl
 * @description 登录登出管理
 * @date 2020-10-23 10:00:07
 */
@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerifyUnit verifyUnit;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    OperateLogService operateLogService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 自动注入verifyService
     */
    @Autowired
    private VerifyService verifyService;

    @Value("${self.debug}")
    private String debug;

    @Value("${self.smsWord}")
    private String smsWord;

    @Value(("${sidPassUserNameList}"))
    private String sidPassUserNameList;

    @Value("${smsCodeNumber}")
    private Integer smsCodeNumber;

    private static final String TRUE_WORD = "true";

    /**
     * 密码登录
     */
    public final static Byte PASSWORD_LOGIN = 1;

    /**
     * 验证码登录
     */
    private final static Byte VERIFICATION_CODE_LOGIN = 2;


    /**
     * 登录
     */
    @LogRecord(operationCode = "session", operationName = "登录系统", serviceType = "login", serviceTypeName = "登录")
    @PostMapping(value = "/session", produces = "application/json;charset=UTF-8")
    public LoginResDTO loginDmp(@RequestBody LoginReqDTO loginReqDTO) {
        /**
         * 手机号校验
         */
        if (loginReqDTO == null || StringUtils.isBlank(loginReqDTO.getUserAccount())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY, "请输入手机号");
        }
        if (!PhoneUtil.isPhone(loginReqDTO.getUserAccount())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入11位手机号码");
        }
        // 密码参数校验
        if (PASSWORD_LOGIN.equals(loginReqDTO.getLoginType()) && (loginReqDTO == null || StringUtils.isBlank(loginReqDTO.getPassword()))) {
            throw new BusinessException(ResultCode.PASSWORD_IS_EMPTY, "请输入密码");
        }
        //临时Aes密钥ID参数校验
        if (loginReqDTO == null || StringUtils.isBlank(loginReqDTO.getKid())) {
            throw new BusinessException(ResultCode.KID_IS_EMPTY, "密钥ID为空");
        }
        // 必须要阅读用户协议
        /*if (!PRIVACY_AGREE.equals(loginReqDTO.getPrivacyAgreement())) {
            throw new BusinessException(ResultCode.PRIVACY_AGREEMENT_ERROR);
        }*/
        // 验证码ID参数校验
        if (loginReqDTO == null || StringUtils.isBlank(loginReqDTO.getSid())) {
            throw new BusinessException(ResultCode.ACCESS_ERROR, "验证码ID参数为空");
        }
        // 验证码参数校验
        if (loginReqDTO == null || StringUtils.isBlank(loginReqDTO.getKaptchaText())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "请输入验证码");
        }
        //图形验证码校验
        if (!verifyUnit.checkKaptchaText(loginReqDTO.getKaptchaText(), loginReqDTO.getSid(), true)) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_ERROR, "验证码错误");
        }

        /**
         * 获取AES密钥信息
         */
        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + loginReqDTO.getKid());
        stringRedisTemplate.delete(SECRET_INFO_KEY + loginReqDTO.getKid());
        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.KID_EXPIRED_ERROR, "密钥ID已过期，请重新获取");
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        if (PASSWORD_LOGIN.equals(loginReqDTO.getLoginType())) {
            // 密码解密
            loginReqDTO.setPassword(aes.getDecodeValue(loginReqDTO.getPassword()));
        }
        // 账号密码校验
        User validateUser = loginService.authDmp(loginReqDTO);
        //设置小程序openid
        if (StringUtils.isNotBlank(loginReqDTO.getOpenid())) {
            validateUser.setOpenid(loginReqDTO.getOpenid());
        }
        // 短信验证码验证
        if (VERIFICATION_CODE_LOGIN.equals(loginReqDTO.getLoginType())) {
            List<String> userNameList = Arrays.asList(sidPassUserNameList.split(","));
            if (!userNameList.contains(loginReqDTO.getUserAccount())) {
                // 手机号验证码参数校验
                if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getSmsCode())) {
                    throw new BusinessException(ResultCode.SMS_TEXT_IS_EMPTY);
                }
                boolean smsCodeVerified = verifyUnit.checkSmsCodeBeforeLogin(CacheConst.SMS_CODE_TEXT_REDIS_KEY + validateUser.getId().getValue(), validateUser.getPhoneNumber().decrypt() + loginReqDTO.getSmsCode());
                // 验证失败
                if (!smsCodeVerified) {
                    throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR);
                }
            }
        }
        LoginResDTO loginResDTO = loginService.loginIdentity(validateUser, request);

        //设置日志操作对象内容
        if (StringUtils.isNotBlank(loginResDTO.getOrgId())) {
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .companyId(Long.valueOf(loginResDTO.getOrgId()))
                    .operationById(loginResDTO.getUserId())
                    .operationByName(loginResDTO.getName())
                    .userType(UserTypeEnum.USER.getValue())
                    .build());
        }

        return loginResDTO;
    }


    /**
     * 退出
     */
    @LogRecord(operationCode = "logOut", operationName = "退出系统", serviceType = "logOut", serviceTypeName = "退出")
    @DeleteMapping(value = "/logOut", produces = "application/json;charset=UTF-8")
    public void logoutUser() {
        loginService.logoutIdentity(request, getOrgId(), getUserId(), getUserName());
    }


    /**
     * 获取短信验证码(登录前)
     *
     * @param
     * @return
     */
    @PostMapping(value = "/verificationCode", produces = "application/json;charset=UTF-8")
    public SimpleStringResDTO verificationCode(@RequestBody @Valid SmsReqDTO smsReqDTO) {
        // 用户账号参数校验
        if (smsReqDTO == null || StringUtils.isEmpty(smsReqDTO.getUserAccount())) {
            // 账号或密码为空
            throw new BusinessException(ResultCode.USER_ACCOUNT_IS_EMPTY);
        }
        // 图形验证码参数校验
        if (smsReqDTO == null || StringUtils.isEmpty(smsReqDTO.getKaptchaText())) {
            // 图形验证码为空
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY);
        }
        // 图形验证码sid参数校验
        if (smsReqDTO == null || StringUtils.isEmpty(smsReqDTO.getSid())) {
            // 图形验证码sid为空
            throw new BusinessException(ResultCode.ACCESS_ERROR);
        }
        // 加密后的用户名或邮箱或者手机号
        String safeAccount = Sm4.encryption(smsReqDTO.getUserAccount());
        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + smsReqDTO.getKid());
        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.SMS_CODE_EXPIRED_ERROR);
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        // 查询用户信息
        UserDTO userDataEntity = userService.getOneUser(smsReqDTO.getUserAccount(), safeAccount);
        Assert.isTrue(Objects.nonNull(userDataEntity), () -> new BusinessException(ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR));
        if (StringUtils.isEmpty(userDataEntity.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY);
        } else {
            //手机号解密
            String phoneNumber = Sm4.decrypt(userDataEntity.getPhoneNumber());
            // 设置redis缓存，key为日期加手机号，value为这个手机号每天发送登录验证码的次数
            String key = CacheConst.LOGIN_SMS_CODE_ONE_DAY_PHONE_NUMBER + LocalDate.now() + ":" + phoneNumber;
            long smsCount = redisUtil.incr(key, Const.NUMBER_1);
            if (smsCount > smsCodeNumber) {
                throw new BusinessException(ResultCode.SMS_COUNT_EXCEEDS_LIMIT);
            }
            redisUtil.expire(key, Const.ONE_DAY_SECOND);
            SimpleStringResDTO simpleStringResDTO;
            Long orgId = userService.getUserOrg(userDataEntity.getId());
            if (Objects.isNull(orgId)) {
                throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
            }
            try {
                simpleStringResDTO = verifyService.sendSmsCodeWithoutKaptcha(phoneNumber, userDataEntity.getId().toString());
            } catch (Exception e) {
                redisUtil.decr(key, Const.NUMBER_1);
                throw e;
            }
            return simpleStringResDTO;
        }
    }
}
