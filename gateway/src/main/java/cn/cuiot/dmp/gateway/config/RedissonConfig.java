package cn.cuiot.dmp.gateway.config;

import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 *
 * @author yht
 */
@Slf4j
@Configuration
public class RedissonConfig {

    @Resource
    private RedissonProperties redissonProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        //存在cluster配置时，优先使用cluster配置
        if (redissonProperties.getCluster() != null
                && redissonProperties.getCluster().getNodes() != null
                && !redissonProperties.getCluster().getNodes().isEmpty()) {
            //redis cluster config
            String[] nodes = redissonProperties.getCluster().getNodes().stream().map(node -> "redis://" + node)
                    .collect(Collectors.toList()).toArray(new String[0]);
            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(nodes);
            if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
                serverConfig.setPassword(redissonProperties.getPassword());
            }
        } else if (redissonProperties.getSentinel() != null
                && redissonProperties.getSentinel().getNodes() != null
                && !redissonProperties.getSentinel().getNodes().isEmpty()) {
            // redis sentinel config
            String[] nodes = redissonProperties.getSentinel().getNodes().stream().map(node -> "redis://" + node)
                    .collect(Collectors.toList()).toArray(new String[0]);
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers().addSentinelAddress(nodes)
                    .setMasterName(redissonProperties.getSentinel().getMaster())
                    .setTimeout(redissonProperties.getTimeout());
            if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
                sentinelServersConfig.setPassword(redissonProperties.getPassword());
            }
        } else {
            //redis single server config
            config.useSingleServer().setAddress(
                    new StringBuilder("redis://")
                            .append(redissonProperties.getHost())
                            .append(":")
                            .append(redissonProperties.getPort())
                            .toString()
            ).setPassword(redissonProperties.getPassword())
            .setPingConnectionInterval(1000);
        }

        return Redisson.create(config);
    }
}
