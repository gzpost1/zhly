package cn.cuiot.dmp.system.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author jiangze
 * @classname StartupRunnerConfig
 * @description CommandLineRunner实现类，服务启动后执行。
 * @date 2020-09-15
 */
@Component
@Slf4j
public class StartupRunnerConfig implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("DMP server successfully started:community-system");
    }
}
