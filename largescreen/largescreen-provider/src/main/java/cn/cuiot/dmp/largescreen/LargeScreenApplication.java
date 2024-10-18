package cn.cuiot.dmp.largescreen;

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
 * system启动类
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@MapperScan(basePackages = {"cn.cuiot.dmp.largescreen.**.mapper", "cn.cuiot.dmp.largescreen.**.entity"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"cn.cuiot.dmp"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"cn.cuiot.dmp"})
@EnableAsync
public class LargeScreenApplication {
    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(LargeScreenApplication.class);
    }
}
