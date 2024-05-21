package cn.cuiot.dmp.app;

import com.alibaba.fastjson2.JSONFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * app应用启动入口
 *
 * @ClassName AppApplication
 * @Description app启动类
 **/
@MapperScan(basePackages = {"cn.cuiot.dmp.**.mapper", "cn.cuiot.dmp.**.dao"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"cn.cuiot.dmp"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"cn.cuiot.dmp"})
@EnableAsync
public class AppApplication {

    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(AppApplication.class);
    }
}
