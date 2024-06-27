package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.auth.ChangePhoneDto;
import cn.cuiot.dmp.system.application.param.dto.auth.PwdChangeDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SampleUserInfoDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeCheckReqDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeCheckResDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeReqDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeResDto;
import cn.cuiot.dmp.system.application.service.AuthService;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.KaptchaResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SecretKeyResDTO;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人中心接口
 *
 * @author: wuyongchong
 * @date: 2024/6/24 15:00
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private AuthService authService;

    /**
     * 获取图形验证码
     */
    @PostMapping("kaptcha")
    public KaptchaResDTO createKaptcha() {
        // 返回图形验证码
        return verifyService.createKaptchaImage();
    }

    /**
     * 获取对称密钥信息
     */
    @PostMapping("secretKey")
    public SecretKeyResDTO createSecretKey() {
        // 返回密钥信息
        return verifyService.createSecretKey();
    }

    /**
     * 设置用户头像与昵称
     */
    @PostMapping("setSampleUserInfo")
    public IdmResDTO setSampleUserInfo(@RequestBody @Valid SampleUserInfoDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        authService.setSampleUserInfo(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 发送手机号验证码
     */
    @PostMapping("sendPhoneSmsCode")
    public IdmResDTO<SmsCodeResDto> sendPhoneSmsCode(@RequestBody @Valid SmsCodeReqDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        // 验证码ID参数校验
        if (StringUtils.isBlank(dto.getSid())) {
            throw new BusinessException(ResultCode.ACCESS_ERROR, "图形验证码ID参数为空");
        }
        // 验证码参数校验
        if (StringUtils.isBlank(dto.getKaptchaText())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "请输入图形验证码");
        }
        //图形验证码校验
        if (!verifyService.checkKaptchaText(dto.getKaptchaText(), dto.getSid())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_ERROR, "图形验证码错误");
        }
        SmsCodeResDto res = verifyService
                .sendPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId());
        return IdmResDTO.success(res);
    }

    /**
     * 校验短信验证码
     */
    @PostMapping("checkPhoneSmsCode")
    public IdmResDTO<SmsCodeCheckResDto> checkPhoneSmsCode(
            @RequestBody @Valid SmsCodeCheckReqDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        String phoneNumber = Optional.ofNullable(dto).map(d -> d.getPhoneNumber()).orElse(null);
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "手机号不能为空");
        }
        String smsCode = Optional.ofNullable(dto).map(d -> d.getSmsCode()).orElse(null);
        if (StringUtils.isBlank(smsCode)) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "短信验证码不能为空");
        }
        SmsCodeCheckResDto res = verifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId(), smsCode, false);
        return IdmResDTO.success(res);
    }

    /**
     * 修改手机号
     */
    @PostMapping("changePhone")
    public IdmResDTO changePhone(@RequestBody @Valid ChangePhoneDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        authService.changePhone(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 修改密码
     */
    @PostMapping("changePwd")
    public IdmResDTO changePwd(@RequestBody @Valid PwdChangeDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        authService.changePwd(dto);
        return IdmResDTO.success(null);
    }

}
