package cn.cuiot.dmp.base.infrastructure.http;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lifei
 * @Description: http转发客户端配置
 * @Date: 2020/9/24
 */
@Configuration
@Data
public class HttpClientConfig {
    private int connectTimeout = 3000;

    private int responseTimeout = 15000;

    private int connectionRequestTimeout = 2000;

    private int pollMaxTotal = 10000;
    
    private int pollMaxPeerRouter = 1000;
}
