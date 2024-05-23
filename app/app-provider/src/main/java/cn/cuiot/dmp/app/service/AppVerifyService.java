package cn.cuiot.dmp.app.service;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;
import static cn.cuiot.dmp.common.constant.ResultCode.KAPTCHA_ERROR;

import cn.cuiot.dmp.app.dto.user.KaptchaResDTO;
import cn.cuiot.dmp.app.dto.user.SecretKeyResDTO;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.SecurityConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.Aes;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2024/5/23 15:23
 */
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

}
