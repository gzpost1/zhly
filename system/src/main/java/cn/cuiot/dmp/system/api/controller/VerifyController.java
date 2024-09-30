package cn.cuiot.dmp.system.api.controller;

import static cn.cuiot.dmp.common.constant.ResultCode.KAPTCHA_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.KAPTCHA_TEXT_IS_EMPTY;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_IS_EMPTY;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.KaptchaResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SecretKeyResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeCheckReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeGenReqDTO;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证控制器
 * @Author xieSH
 * @Description 验证控制器
 * @Date 2021/8/9 15:36 
 * @param
 * @return
 **/
@Slf4j
@RestController
public class VerifyController extends BaseController {

    /**
     * 自动注入verifyService
     */
    @Autowired
    private VerifyService verifyService;

    /**
     * 自动注入userService
     */
    @Autowired
    private UserService userService;

    /**
     * 自动注入verifyUnit
     */
    @Autowired
    private VerifyUnit verifyUnit;

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${self.debug}")
    private String debug;

    /**
     * 非
     */
    private static final String FALSE = "false";

    @PostMapping(value = "/secretKey", produces = "application/json;charset=UTF-8")
    public SecretKeyResDTO createSecretKey() {
        // 返回密钥信息
        return verifyService.createSecretKey();
    }

    /**
     * 创建并获取图形验证码
     *
     * @param
     * @return
     */
    @PostMapping(value = "/kaptcha", produces = "application/json;charset=UTF-8")
    public KaptchaResDTO createKaptcha() {
        // 返回图形验证码
        return verifyService.createKaptchaImage();
    }

    /**
     * 获取短信验证码（修改手机号第一步：身份验证） 首次绑定手机号码
     *
     * @return
     */
    @PostMapping(value = "/sms-code/genBindPhone", produces = "application/json;charset=UTF-8")
    public SimpleStringResDTO genBindPhone(@RequestBody SmsCodeGenReqDTO smsCodeGenReqDTO) {
        // 获取session中的userId
        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }

        String phoneNumber = smsCodeGenReqDTO.getPhoneNumber();
        // 验证码参数校验
        if (smsCodeGenReqDTO == null || StringUtils.isEmpty(smsCodeGenReqDTO.getKaptchaText())) {
            // 图形验证码为空
            throw new BusinessException(KAPTCHA_TEXT_IS_EMPTY);
        }
        boolean kaptchaVerified = true;
        // 图形验证码校验
        if (FALSE.equals(debug)) {
            kaptchaVerified = verifyUnit.checkKaptchaText(smsCodeGenReqDTO.getKaptchaText(), smsCodeGenReqDTO.getSid(),false);
        }
        if (kaptchaVerified) {
            return verifyService.sendSmsCodeUpdatePhone(phoneNumber, userId);
        } else {
            throw new BusinessException(KAPTCHA_TEXT_ERROR);
        }
    }

    /**
     * 获取短信验证码（修改手机号第一步：身份验证）
     *
     * @return
     */
    @PostMapping(value = "/sms-code/genUpdatePhone", produces = "application/json;charset=UTF-8")
    public SimpleStringResDTO genUpdatePhone(@RequestBody SmsCodeGenReqDTO smsCodeGenReqDTO) {
        // 获取session中的userId
        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }

        // 验证码参数校验
        if (smsCodeGenReqDTO == null || StringUtils.isEmpty(smsCodeGenReqDTO.getKaptchaText())) {
            // 图形验证码为空
            throw new BusinessException(KAPTCHA_TEXT_IS_EMPTY);
        }
        boolean kaptchaVerified = true;
        // 图形验证码校验
        if (FALSE.equals(debug)) {
            kaptchaVerified = verifyUnit.checkKaptchaText(smsCodeGenReqDTO.getKaptchaText(), smsCodeGenReqDTO.getSid(),false);
        }
        if (kaptchaVerified) {

            //0.9改动原手机号参数去除，由后端自行获取--2020/12/07
            String phoneNumber = Sm4.decrypt(userService.getUserById(userId).getPhoneNumber());

            return verifyService.sendSmsCodeUpdatePhone(phoneNumber, userId);
        } else {
            throw new BusinessException(KAPTCHA_TEXT_ERROR);
        }

    }

    /**
     * 验证短信验证码（修改手机号第二步：身份验证）
     *
     * @param
     * @return
     */
    @PostMapping(value = "/sms-code/checkUpdatePhone", produces = "application/json;charset=UTF-8")
    public SimpleStringResDTO checkOldPhone(@RequestBody SmsCodeCheckReqDTO smsCodeCheckReqDTO) {
        // 短信验证码
        String smsCode = Optional.ofNullable(smsCodeCheckReqDTO).map(SmsCodeCheckReqDTO::getSmsCode).orElse(null);
        if (StringUtils.isEmpty(smsCode)) {
            // 短信验证码为空
            throw new BusinessException(SMS_TEXT_IS_EMPTY);
        }
        // 获取session中的userId
        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }
        //0.9改动原手机号参数去除，由后端自行获取--2020/12/07
        String phoneNumber = Sm4.decrypt(userService.getUserById(userId).getPhoneNumber());
        boolean smsCodeVerified = true;
        if (FALSE.equals(debug)) {
            // 短信验证码验证
            smsCodeVerified = verifyUnit.checkSmsCode(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber, phoneNumber + smsCode);
        }
        // 验证成功
        if (smsCodeVerified) {
            return new SimpleStringResDTO("验证成功");
        }
        // 验证失败
        else {
            // 安全要求： 短信验证码防暴力破解，关键操作每提交一次请求，应发送新的短信验证码，并且不可继续使用旧的验证码
            stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
            throw new BusinessException(SMS_TEXT_ERROR);
        }
    }

}
