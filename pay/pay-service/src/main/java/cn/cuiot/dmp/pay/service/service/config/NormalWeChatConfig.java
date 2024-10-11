package cn.cuiot.dmp.pay.service.service.config;

import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Data
@Slf4j
public class NormalWeChatConfig extends WeChatConfig{

    @Bean("NormalCertificatesVerifier")
    public AutoUpdateCertificatesVerifier getCertificatesVerifier() {
        return new AutoUpdateCertificatesVerifier();
    }
}
