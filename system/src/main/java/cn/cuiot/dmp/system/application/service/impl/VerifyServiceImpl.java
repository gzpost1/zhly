package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.constant.SendMessageConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeCheckResDto;
import cn.cuiot.dmp.system.application.param.dto.auth.SmsCodeResDto;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.repository.UserRepository;
import cn.cuiot.dmp.system.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.infrastructure.entity.SensitivityEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.KaptchaResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SecretKeyResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SensitiveDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.utils.SensitiveWordEngineUtils;
import cn.cuiot.dmp.system.infrastructure.utils.SensitiveWordInitUtils;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.*;


/**
 * @author jiangze
 * @classname VerifyServiceImpl
 * @description 验证业务实现
 * @date 2020-06-22
 */
@Slf4j
@Service
public class VerifyServiceImpl implements VerifyService {

    /**
     * 自动注入stringRedisTemplate
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 自动注入defaultKaptcha
     */
    @Autowired
    private Producer defaultKaptcha;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPhoneNumberDomainService userPhoneNumberDomainService;

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

    @Autowired
    private UserDao userDao;

    /**
     * 自动注入stringRedisTemplate
     * sensitiveWordInitUtils
     * sensitiveWordEngineUtils
     * 2020/12/10
     */
    @Autowired
    private SensitiveDao sensitiveDao;

    @Autowired
    private SensitiveWordInitUtils sensitiveWordInitUtils;

    private static final String PARTNER_CODE = "00000000";

    @Override
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
        redisUtil.set(CacheConst.KAPTCHA_TEXT_REDIS_KEY + kaptchaResDTO.getSid(), capText, SecurityConst.KAPTCHA_EXPIRED_TIME);
        // 返回验证码
        return kaptchaResDTO;
    }

    @Override
    public SecretKeyResDTO createSecretKey() {
        String kid = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        Aes aes = new Aes(RandomStringUtils.randomAlphabetic(16), RandomStringUtils.randomAlphabetic(16));
        stringRedisTemplate.opsForValue().set(SECRET_INFO_KEY + kid, JSONObject.toJSONString(aes), 2 * SecurityConst.SMS_CODE_EXPIRED_TIME, TimeUnit.MINUTES);
        return new SecretKeyResDTO(kid, aes.getSecretKey(), aes.getIv());
    }

    /**
     * 发送短信验证码（无图片验证码）
     *
     * @param phoneNumber
     * @param userId
     * @return
     */
    @Override
    public SimpleStringResDTO sendSmsCodeWithoutKaptcha(String phoneNumber, String userId) {
        // 查看redis中是否已经存在短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue().get(CacheConst.SMS_ALREADY_SEND_REDIS_KEY + phoneNumber);
        // 短时间内重复获取
        if (!StringUtils.isEmpty(expectedText)) {
            // 短时间内重复提交
            throw new BusinessException(SMS_CODE_FREQUENTLY_REQ_ERROR);
        }
        // 生成短信验证码
        String smsCode = RandomStringUtils.random(6, false, true);
        Long orgId = userDao.getOrgId(Long.parseLong(userId));
        if (Objects.isNull(orgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        // 发送短信
        boolean sendSucceed = verifyUnit.sendSmsCode(smsCode, phoneNumber, orgId);

        // 发送成功
        if (sendSucceed) {
            // 存入redis并设置过期时间
            stringRedisTemplate.opsForValue().set(CacheConst.SMS_ALREADY_SEND_REDIS_KEY + phoneNumber, phoneNumber + smsCode, SecurityConst.SMS_CODE_FORBIDDEN_TIME, TimeUnit.MINUTES);
            // 短信验证码以登录用户的标记存入redis
            stringRedisTemplate.opsForValue().set(CacheConst.SMS_CODE_TEXT_REDIS_KEY + userId, phoneNumber + smsCode, SecurityConst.SMS_CODE_EXPIRED_TIME, TimeUnit.MINUTES);
            return new SimpleStringResDTO("发送成功", null, null);
        }
        return new SimpleStringResDTO("发送失败");
    }

    /**
     * 修改手机号第一步：身份验证（短信验证码验证）
     *
     * @param phoneNumber
     * @param userId
     * @return
     */
    @Override
    public SimpleStringResDTO sendSmsCodeUpdatePhone(String phoneNumber, String userId) {
        // 查看redis中是否已经存在短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue().get(CacheConst.SMS_ALREADY_SEND_REDIS_KEY_P + phoneNumber);
        // 短时间内重复获取
        if (!StringUtils.isEmpty(expectedText)) {
            // 短时间内重复提交
            throw new BusinessException(SMS_CODE_FREQUENTLY_REQ_ERROR);
        }
        //首次判断手机号是否为空
        User user = userRepository.find(new UserId(userId));
        PhoneNumber oldPhoneNumber = user.getPhoneNumber();
        if (oldPhoneNumber == null && userPhoneNumberDomainService.judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber))) {
            // 判断手机号是否存在
            throw new BusinessException(PHONE_NUMBER_EXIST);
        }
        // 生成短信验证码
        String smsCode = RandomStringUtils.random(6, false, true);
        Long orgId = userDao.getOrgId(Long.parseLong(userId));
        if (Objects.isNull(orgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        // 发送短信
        boolean sendSucceed = verifyUnit.sendSmsCode(String.format(SendMessageConst.SEND_MESSAGE_TEMPLATE, smsCode), phoneNumber, orgId);
        // 发送成功
        if (sendSucceed) {
            // 存入redis并设置过期时间
            stringRedisTemplate.opsForValue().set(CacheConst.SMS_ALREADY_SEND_REDIS_KEY_P + phoneNumber, phoneNumber + smsCode, SecurityConst.SMS_CODE_FORBIDDEN_TIME, TimeUnit.MINUTES);
            // 短信验证码以登录用户的标记存入redis
            stringRedisTemplate.opsForValue().set(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber, phoneNumber + smsCode, SecurityConst.SMS_CODE_EXPIRED_TIME, TimeUnit.MINUTES);
            return new SimpleStringResDTO("发送成功", null, null);
        }
        return new SimpleStringResDTO("发送失败");
    }

    @Override
    public boolean searchSensitiveWord(String username) {
        //从数据库获取敏感词
        List<SensitivityEntity> sensitiveWords = sensitiveDao.searchSensitiveWord();
        //初始化敏感词库
        Map sensitiveWordMap = sensitiveWordInitUtils.initKeyWord(sensitiveWords);
        SensitiveWordEngineUtils.sensitiveWordMap = sensitiveWordMap;
        //校验是否含有敏感词
        return SensitiveWordEngineUtils.isContaintSensitiveWord(username, 2);
    }


    /**
     * 验证图形验证码
     *
     * @param actualText 用户输入到验证码
     * @param uuid       身份识别id
     */
    @Override
    public boolean checkKaptchaText(String actualText, String uuid) {
        // 获取redis中的图形验证码文本
        String expectedText = stringRedisTemplate.opsForValue()
                .get(CacheConst.KAPTCHA_TEXT_REDIS_KEY + uuid);
        // 判断是否过期
        if (org.springframework.util.StringUtils.isEmpty(expectedText)) {
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
    @Override
    public SmsCodeResDto sendPhoneSmsCode(String phoneNumber, Long userId) {
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
        Long orgId = userDao.getOrgId(userId);
        if (Objects.isNull(orgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        // 发送短信
        boolean sendSucceed = verifyUnit.sendSmsCode(String.format(SendMessageConst.SEND_MESSAGE_TEMPLATE, smsCode), phoneNumber, orgId);
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
    @Override
    public SmsCodeCheckResDto checkPhoneSmsCode(String phoneNumber, Long userId, String smsCode, Boolean needDeleteCache) {
        // 获取redis中的短信验证码文本
        String expectedText = stringRedisTemplate.opsForValue()
                .get(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
        // 判断是否过期
        if (org.springframework.util.StringUtils.isEmpty(expectedText)) {
            // 短信验证码过期
            throw new BusinessException(SMS_CODE_EXPIRED_ERROR);
        }
        if (Boolean.TRUE.equals(needDeleteCache)) {
            stringRedisTemplate.delete(CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumber);
        }
        // 判断用户输入的验证码是否正确
        Boolean checkSucceed = expectedText.equals(phoneNumber + smsCode);
        if (checkSucceed) {
            return new SmsCodeCheckResDto(checkSucceed, "校验成功");
        }
        return new SmsCodeCheckResDto(checkSucceed, "校验失败");
    }
}
