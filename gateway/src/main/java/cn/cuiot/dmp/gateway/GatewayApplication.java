package cn.cuiot.dmp.gateway;

import com.alibaba.fastjson2.JSONFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhangxp207
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(GatewayApplication.class);
    }
}
