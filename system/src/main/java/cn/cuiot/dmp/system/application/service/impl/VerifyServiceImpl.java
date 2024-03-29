package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.KAPTCHA_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PHONE_NUMBER_EXIST;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_CODE_FREQUENTLY_REQ_ERROR;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.constant.SendMessageConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorker;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.service.VerifyService;
import cn.cuiot.dmp.system.infrastructure.entity.SensitivityEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.KaptchaResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SecretKeyResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SensitiveDao;
import cn.cuiot.dmp.system.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.infrastructure.utils.SensitiveWordEngineUtils;
import cn.cuiot.dmp.system.infrastructure.utils.SensitiveWordInitUtils;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.service.UserPhoneNumberDomainService;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * @author jiangze
 * @classname VerifyServiceImpl
 * @description 验证业务实现
 * @date 2020-06-22
 */
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

    /**
     * 雪花算法生成器
     */
    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    @Override
    public KaptchaResDTO createKaptchaImage() {
        // 生成文本验证码
        String capText = defaultKaptcha.createText();
        // 根据文本验证码生成图形验证码
        BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
        // 创建验证码
        KaptchaResDTO kaptchaResDTO = new KaptchaResDTO();
        kaptchaResDTO.setSid(String.valueOf(idWorker.nextId()));
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
        String kid = String.valueOf(idWorker.nextId());
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
        // 发送短信
        boolean sendSucceed = verifyUnit.sendSmsCode(String.format(SendMessageConst.SEND_MESSAGE_TEMPLATE, smsCode), phoneNumber);

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
        if (oldPhoneNumber == null && userPhoneNumberDomainService.judgePhoneNumberAlreadyExists(new PhoneNumber(phoneNumber))){
            // 判断手机号是否存在
            throw new BusinessException(PHONE_NUMBER_EXIST);
        }
        // 生成短信验证码
        String smsCode = RandomStringUtils.random(6, false, true);
        // 发送短信
        boolean sendSucceed = verifyUnit.sendSmsCode(String.format(SendMessageConst.SEND_MESSAGE_TEMPLATE, smsCode), phoneNumber);
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
}
