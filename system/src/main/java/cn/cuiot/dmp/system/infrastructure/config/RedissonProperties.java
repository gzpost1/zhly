package cn.cuiot.dmp.system.infrastructure.config;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redisson配置对象
 *
 * @ClassName RedissonProperties
 * @Description redisson配置对象
 * @Author dengmawei
 * @Date 2020/9/24 9:13
 * @Version 1.0
 **/
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class RedissonProperties {
    /**
     * redis集群节点
     */
    private Cluster cluster;

    /**
     * redis哨兵节点
     */
    private RedisProperties.Sentinel sentinel;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * redis密码
     */
    private String password;
    
    /**
     * 单节点redis配置
     */
    private String host;
    private int port;

    /**
     * 集群配置
     */
    @Data
    public static class Cluster {
        /**
         * 集群节点
         */
        private List<String> nodes;
    }
}
