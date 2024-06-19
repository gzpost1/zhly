package cn.cuiot.dmp.content;//	模板

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
 * @author hantingyao
 * @Description
 * @data 2024/5/30 11:36
 */
@MapperScan(basePackages = {"cn.cuiot.dmp.content.dal.mapper",})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"cn.cuiot.dmp"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"cn.cuiot.dmp"})
@EnableAsync
public class ContentManageApplication {
    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(ContentManageApplication.class);
    }
}
