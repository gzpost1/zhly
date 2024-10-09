package cn.cuiot.dmp.externalapi.service.vendor.hik.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 海康配置信息
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
@ConfigurationProperties(prefix = HikProperties.PREFIX)
@Component
@RefreshScope
public class HikProperties implements Serializable {

    public static final String PREFIX = "hik";

    /**
     * 海康host
     */
    @Value("${hik.host}")
    private String host;

    /**
     * 海康artemisPath
     */
    @Value("${hik.artemisPath}")
    private String artemisPath;
}
