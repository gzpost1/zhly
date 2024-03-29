package cn.cuiot.dmp.system.infrastructure.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @classname KaptchaConfig
 * @description 图形验证码配置
 * @author jiangze
 * @date 2020-06-18
 */
@Configuration
public class KaptchaConfig {

    /**
     * RestTemplate Bean
     * @param
     * @return
     */
    @Bean
    public DefaultKaptcha defaultKaptcha() {
        // 创建DefaultKaptcha
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 创建属性
        Properties properties = new Properties();
        // 宽度
        properties.setProperty("kaptcha.image.width", "180");
        // 高度
        properties.setProperty("kaptcha.image.height", "50");
        // 创建配置文件
        Config config = new Config(properties);
        // 设置验证码配置信息
        defaultKaptcha.setConfig(config);
        // 返回验证码对象
        return defaultKaptcha;
    }
}
