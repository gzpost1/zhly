package cn.cuiot.dmp.system.infrastructure.utils;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.SmsStdTemplate;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.query.SmsSendQuery;
import cn.cuiot.dmp.sms.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

import static cn.cuiot.dmp.common.constant.ResultCode.*;

import static cn.cuiot.dmp.common.constant.ResultCode.KAPTCHA_EXPIRED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_EXPIRED_ERROR;


/**
 * @author jiangze
 * @classname VerifyUnit
 * @description 验证工具
 * @date 2020-06-23
 */
@Component
@Slf4j
public class VerifyUnit {

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SmsSendService smsSendService;

    /**
     * 验证图形验证码
     *
     * @param actualText 用户输入到验证码
     * @param uuid       身份识别id
     * @return
     */
    public boolean checkKaptchaText(String actualText, String uuid, Boolean deleteKaptcha) {
        // 获取redis中的图形验证码文本
        String expectedText = stringRedisTemplate.opsForValue().get(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
        // 判断是否过期
        if (StringUtils.isEmpty(expectedText)) {
            // 图形验证码过期
            throw new BusinessException(KAPTCHA_EXPIRED_ERROR);
        }
        // 判断用户输入的验证码是否正确
        if (!expectedText.equals(actualText)) {
            stringRedisTemplate.delete(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
            // 图形验证码错误
            return false;
        }
        if (deleteKaptcha) {
            stringRedisTemplate.delete(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
        }
        return true;
    }


    /**
     * 验证短信验证码（通用）
     *
     * @param redisKey    redis验证码key值
     * @param checkedText 要验证的值
     * @return
     */
    public boolean checkSmsCode(String redisKey, String checkedText) {
        // 获取redis中的短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue().get(redisKey);
        // 判断是否过期
        if (StringUtils.isEmpty(expectedText)) {
            // 短信验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        // 判断用户输入的验证码是否正确
        return expectedText.equals(checkedText);
    }

    /**
     * 验证短信验证码（通用）
     *
     * @param redisKey    redis验证码key值
     * @param checkedText 要验证的值
     * @return
     */
    public boolean checkSmsCodeBeforeLogin(String redisKey, String checkedText) {
        // 获取redis中的短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue().get(redisKey);
        // 判断是否过期
        if (StringUtils.isEmpty(expectedText)) {
            // 短信验证码过期
            return false;
        }
        stringRedisTemplate.delete(redisKey);
        // 判断用户输入的验证码是否正确
        return expectedText.equals(checkedText);
    }

    /**
     * 发送短信验证码
     *
     * @param message     短信内容
     * @param phoneNumber 手机号
     * @return
     */
    public boolean sendSmsCode(String message, String phoneNumber, Long companyId) {
        SmsSendQuery query = new SmsSendQuery();
        query.setCompanyId(companyId).setMobile(phoneNumber).setParams(Collections.singletonList(message)).setStdTemplate(SmsStdTemplate.MANAGE_LOGIN_OR_UPDATE_PASSWORD);
        log.info("发送短信验证码：{}", JsonUtil.writeValueAsString(query));
        try {
            smsSendService.sendMsg(query);
        } catch (Exception ex) {
            log.info("发送短信验证码失败:", ex);
            throw new BusinessException(SMS_NOT_ENABLED);
        }
        return true;
    }

}
