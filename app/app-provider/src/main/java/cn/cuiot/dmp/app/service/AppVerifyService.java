package cn.cuiot.dmp.app.service;

import cn.cuiot.dmp.app.dto.user.KaptchaResDTO;
import cn.cuiot.dmp.app.dto.user.SecretKeyResDTO;
import cn.cuiot.dmp.app.dto.user.SmsCodeCheckResDto;
import cn.cuiot.dmp.app.dto.user.SmsCodeResDto;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.constant.SendMessageConst;
import cn.cuiot.dmp.common.constant.SmsStdTemplate;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.sms.query.SmsSendQuery;
import cn.cuiot.dmp.sms.service.SmsSendService;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.*;

/**
 * @author: wuyongchong
 * @date: 2024/5/23 15:23
 */
@Slf4j
@Service
public class AppVerifyService {

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 自动注入defaultKaptcha
     */
    @Autowired
    private Producer defaultKaptcha;
    @Autowired
    private SmsSendService smsSendService;

    /**
     * 生成图形验证码
     */
    public KaptchaResDTO createKaptchaImage() {
        // 生成文本验证码
        String capText = defaultKaptcha.createText();
        // 根据文本验证码生成图形验证码
        BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
        // 创建验证码
        KaptchaResDTO kaptchaResDTO = new KaptchaResDTO();
        kaptchaResDTO.setSid(String.valueOf(SnowflakeIdWorkerUtil.nextId()));
        // 将BufferedImage转为Base64编码图片
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 将BufferedImage写入outputStream
            ImageIO.write(bufferedImage, "jpg", outputStream);
            // 将outputStream中的二进制字节流进行base64编码
            String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            // 添加文件格式
            String imageBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r|\n", "");
            // 设置验证码图片
            kaptchaResDTO.setImageBase64(imageBase64);
        } catch (IOException e) {
            // 抛出异常
            throw new BusinessException(KAPTCHA_ERROR);
        }
        // 存入redis并设置过期时间
        redisUtil.set(CacheConst.KAPTCHA_TEXT_REDIS_KEY + kaptchaResDTO.getSid(), capText,
                SecurityConst.KAPTCHA_EXPIRED_TIME);
        // 返回验证码
        return kaptchaResDTO;
    }

    /**
     * 生成对称密钥
     */
    public SecretKeyResDTO createSecretKey() {
        String kid = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        Aes aes = new Aes(RandomStringUtils.randomAlphabetic(16),
                RandomStringUtils.randomAlphabetic(16));
        stringRedisTemplate.opsForValue().set(SECRET_INFO_KEY + kid, JSONObject.toJSONString(aes),
                2 * SecurityConst.SMS_CODE_EXPIRED_TIME, TimeUnit.MINUTES);
        return new SecretKeyResDTO(kid, aes.getSecretKey(), aes.getIv());
    }


    /**
     * 验证图形验证码
     *
     * @param actualText 用户输入到验证码
     * @param uuid       身份识别id
     */
    public boolean checkKaptchaText(String actualText, String uuid) {
        // 获取redis中的图形验证码文本
        String expectedText = stringRedisTemplate.opsForValue()
                .get(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
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
        stringRedisTemplate.delete(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
        return true;
    }

    /**
     * 发送短信验证码
     */
    public SmsCodeResDto sendPhoneSmsCode(String phoneNumber, Long userId, Long companyId) {
        // 查看redis中是否已经存在短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue()
                .get(CacheConst.SMS_ALREADY_SEND_REDIS_KEY_P + phoneNumber);
        // 短时间内重复获取
        if (!org.apache.commons.lang.StringUtils.isEmpty(expectedText)) {
            // 短时间内重复提交
            throw new BusinessException(SMS_CODE_FREQUENTLY_REQ_ERROR);
        }
        // 生成短信验证码
        String smsCode = RandomStringUtils.random(SendMessageConst.SMS_CODE_LENGTH, false, true);
        log.warn("sendPhoneSmsCode===smsCode:{}", smsCode);
        // 发送短信
        boolean sendSucceed = sendSmsCode(smsCode, phoneNumber, companyId);
        // 发送成功
        if (sendSucceed) {
            // 存入redis并设置过期时间
            stringRedisTemplate.opsForValue()
                    .set(CacheConst.SMS_ALREADY_SEND_REDIS_KEY_P + phoneNumber,
                            phoneNumber + smsCode,
                            SecurityConst.SMS_CODE_FORBIDDEN_TIME, TimeUnit.MINUTES);
            // 短信验证码以登录用户的标记存入redis
            stringRedisTemplate.opsForValue()
                    .set(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber,
                            phoneNumber + smsCode,
                            SecurityConst.SMS_CODE_EXPIRED_TIME, TimeUnit.MINUTES);

            return new SmsCodeResDto(sendSucceed, "发送成功");
        }
        return new SmsCodeResDto(sendSucceed, "发送失败");
    }

    /**
     * 校验短信验证码
     */
    public SmsCodeCheckResDto checkPhoneSmsCode(String phoneNumber, Long userId, String smsCode, Boolean needDeleteCache) {
        // 获取redis中的短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue()
                .get(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
        // 判断是否过期
        if (StringUtils.isEmpty(expectedText)) {
            // 短信验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        // 判断用户输入的验证码是否正确
        Boolean checkSucceed = expectedText.equals(phoneNumber + smsCode);
        if (checkSucceed) {
            if (Boolean.TRUE.equals(needDeleteCache)) {
                stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
            }
            return new SmsCodeCheckResDto(checkSucceed, "校验成功");
        }
        return new SmsCodeCheckResDto(checkSucceed, "校验失败");
    }

    /**
     * 发送短信验证码
     *
     * @param smsCode     验证码
     * @param phoneNumber 手机号
     * @return
     */
    public boolean sendSmsCode(String smsCode, String phoneNumber, Long companyId) {
        try {
            SmsSendQuery query = new SmsSendQuery();
            query.setCompanyId(companyId).setMobile(phoneNumber).setParams(Collections.singletonList(smsCode)).setStdTemplate(SmsStdTemplate.CLIENT_LOGIN);
            log.info("发送短信验证码：{}", JsonUtil.writeValueAsString(query));
            smsSendService.sendMsg(query);
        } catch (Exception ex) {
            log.error("发送短信验证码失败", ex);
            return false;
        }
        return true;
    }

}
