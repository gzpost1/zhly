package cn.cuiot.dmp.system.api.controller;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.service.OperateLogService;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.system.application.param.dto.UserDTO;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.LoginService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsReqDTO;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
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
     * 同意用户隐私协议
     */
    private static final String PRIVACY_AGREE = "1";

    /**
     * 登录
     *
     * @param loginReqDTO 请求登录的用户信息
     * @return
     */
    @PostMapping(value = "/session", produces = "application/json;charset=UTF-8")
    public LoginResDTO loginDmp(@RequestBody LoginReqDTO loginReqDTO) {
        log.info("pc用户登录信息 {}", loginReqDTO.getUserAccount());
        // 用户账号参数校验
        if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getUserAccount())) {
            // 账号或密码为空
            throw new BusinessException(ResultCode.USER_ACCOUNT_IS_EMPTY);
        }
        // 用户密码参数校验
        if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getPassword())) {
            // 账号或密码为空
            throw new BusinessException(ResultCode.PASSWORD_IS_EMPTY);
        }
        // 验证码参数校验
        if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getKaptchaText())) {
            // 图形验证码为空
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY);
        }
        // 验证码参数校验
        if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getSid())) {
            // 图形验证码为空
            throw new BusinessException(ResultCode.ACCESS_ERROR);
        }
        if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getKid())) {
            // 图形验证码为空
            throw new BusinessException(ResultCode.KID_IS_EMPTY);
        }
        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + loginReqDTO.getKid());
        stringRedisTemplate.delete(SECRET_INFO_KEY + loginReqDTO.getKid());
        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.KID_EXPIRED_ERROR);
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        // 密码解密
        loginReqDTO.setPassword(aes.getDecodeValue(loginReqDTO.getPassword()));
        // 账号密码校验
        User validateUser = loginService.authDmp(loginReqDTO);
        // 短信验证码验证
        if (TRUE_WORD.equals(smsWord)) {
            List<String> userNameList = Arrays.asList(sidPassUserNameList.split(","));
            if (!userNameList.contains(loginReqDTO.getUserAccount())) {
                // 手机号验证码参数校验
                if (loginReqDTO == null || StringUtils.isEmpty(loginReqDTO.getSmsCode())) {
                    throw new BusinessException(ResultCode.SMS_TEXT_IS_EMPTY);
                }
                boolean smsCodeVerified = verifyUnit.checkSmsCodeBeforeLogin(CacheConst.SMS_CODE_TEXT_REDIS_KEY + validateUser.getUserId(), validateUser.getPhoneNumber().decrypt() + loginReqDTO.getSmsCode());
                // 验证失败
                if (!smsCodeVerified) {
                    throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR);
                }
            }
            // 必须要阅读用户协议
            if (!PRIVACY_AGREE.equals(loginReqDTO.getPrivacyAgreement())) {
                throw new BusinessException(ResultCode.PRIVACY_AGREEMENT_ERROR);
            }
        }

        return loginService.loginIdentity(validateUser, request);
    }


    /**
     * 登出dmp
     *
     * @param
     * @return
     */
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
        // 手机验证码参数校验
        if (smsReqDTO == null || StringUtils.isEmpty(smsReqDTO.getSid())) {
            // 手机验证码为空
            throw new BusinessException(ResultCode.ACCESS_ERROR);
        }
        boolean kaptchaVerified = true;
        // 图形验证码校验
        if (TRUE_WORD.equals(smsWord)) {
            kaptchaVerified = verifyUnit.checkKaptchaText(smsReqDTO.getKaptchaText(), smsReqDTO.getSid());
        }
        if (kaptchaVerified){
            // 加密后的用户名或邮箱或者手机号
            String safeAccount = Sm4.encryption(smsReqDTO.getUserAccount());
            String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + smsReqDTO.getKid());
            if (StringUtils.isEmpty(jsonObject)) {
                throw new BusinessException(ResultCode.SMS_CODE_EXPIRED_ERROR);
            }
            Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
            // 查询用户信息
            UserDTO userDataEntity = userService.getOneUser(smsReqDTO.getUserAccount(), safeAccount, aes.getDecodeValue(smsReqDTO.getPassword()));
            Assert.isTrue(Objects.nonNull(userDataEntity), () -> new BusinessException(ResultCode.USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR));
            if (StringUtils.isEmpty(userDataEntity.getPhoneNumber())){
                throw new BusinessException(ResultCode.PHONE_NUMBER_IS_EMPTY);
            }else {
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
                try {
                    simpleStringResDTO = verifyService.sendSmsCodeWithoutKaptcha(phoneNumber, userDataEntity.getUserId());
                } catch (Exception e) {
                    redisUtil.decr(key, Const.NUMBER_1);
                    throw e;
                }
                return simpleStringResDTO;
            }
        }else{
            throw new BusinessException(USER_ACCOUNT_OR_PASSWORD_ERROR_OR_CODE_ERROR);
        }
    }
}
