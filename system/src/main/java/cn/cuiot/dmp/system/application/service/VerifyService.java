package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeCheckResDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.KaptchaResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SecretKeyResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;

/**
 * @author jiangze
 * @classname VerifyService
 * @description 验证业务接口
 * @date 2020-06-22
 */
public interface VerifyService {

    /**
     * 生成随机图形验证码
     *
     * @return
     */
    KaptchaResDTO createKaptchaImage();

    /**
     * 生成随机密钥
     *
     * @return
     */
    SecretKeyResDTO createSecretKey();

    /**
     * 发送短信验证码（无图片验证码）
     *
     * @param phoneNumber
     * @param userId
     * @return
     */
    SimpleStringResDTO sendSmsCodeWithoutKaptcha(String phoneNumber, String userId);

    /**
     * 修改手机号第一步：身份验证
     *
     * @param phoneNumber
     * @param userId
     * @return
     */
    SimpleStringResDTO sendSmsCodeUpdatePhone(String phoneNumber, String userId);

    /**
     * 敏感词校验
     * 2020/12/10
     *
     * @param username
     * @return
     */
    boolean searchSensitiveWord(String username);

    /**
     * 验证图形验证码
     * @param kaptchaText
     * @param sid
     * @return
     */
    boolean checkKaptchaText(String kaptchaText, String sid);

    /**
     * 发送短信验证码
     * @param phoneNumber
     * @param userId
     * @return
     */
    SmsCodeResDto sendPhoneSmsCode(String phoneNumber, Long userId);

    /**
     * 校验短信验证码
     */
    SmsCodeCheckResDto checkPhoneSmsCode(String phoneNumber, Long userId, String smsCode,Boolean needDeleteCache);
}
