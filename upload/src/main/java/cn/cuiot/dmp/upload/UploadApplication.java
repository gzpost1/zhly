package cn.cuiot.dmp.upload;

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
 * 文件上传服务
 * @ClassName UploadApplication
 * @Description Upload启动类
 * @Author wensq
 * @Date 2024/4/1
 **/
@MapperScan(basePackages = {"cn.cuiot.dmp.upload.infrastructure.persistence.mapper","cn.cuiot.dmp.upload.infrastructure.persistence.dao"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"cn.cuiot.dmp"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableAsync
public class UploadApplication {
    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(UploadApplication.class);
    }
}
