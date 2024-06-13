package cn.cuiot.dmp.app.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * zhh
 * @author 自动生成密码配置类
 * @version 1.0
 * @date 2020/9/3 17:18
 */
@Component
@Slf4j
public class RandomPwUtils {

    private static final int EIGHT = 8;

    private Random random = new SecureRandom();

    public RandomPwUtils() throws NoSuchAlgorithmException {
    }

    public String getRandomPassword(int len) {
        String result = null;
        while (len >= EIGHT) {
            result = this.makeRandomPassword(len);
            if (result.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*.?]).{8,20}$")) {
                return result;
            } else {
                log.warn("RandomPwUtils not match rule:{}, length:{}", result, len);
            }
        }
        log.info("RandomPwUtils new password:{}", result);
        return "长度不得少于8位!";
    }

    public String makeRandomPassword(int len) {
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[random.nextInt(charr.length)]);
        }
        return sb.toString();
    }
}
