package cn.cuiot.dmp.system;

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
 * @ClassName SystemApplication
 * @Description system启动类
 * @Author wensq
 * @Date 2021/12/21 9:13
 * @Version 1.0
 **/
@MapperScan(basePackages = {"cn.cuiot.dmp.system.infrastructure.persistence.mapper","cn.cuiot.dmp.system.infrastructure.persistence.dao"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"cn.cuiot.dmp.system","cn.cuiot.dmp.common.aop","cn.cuiot.dmp.common.log","cn.cuiot.dmp.common.interceptor"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableAsync
public class SystemApplication {
    public static void main(String[] args) {
        JSONFactory.setUseJacksonAnnotation(false);
        SpringApplication.run(SystemApplication.class);
    }
}
