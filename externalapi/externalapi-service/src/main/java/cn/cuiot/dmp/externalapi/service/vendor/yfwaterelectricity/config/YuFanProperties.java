
package cn.cuiot.dmp.externalapi.service.vendor.yfwaterelectricity.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 格物平台关键配置
 * @Date 2024-09-29 16:55
 * @Author by xiaotao
 */
@Data
@ConfigurationProperties(prefix = YuFanProperties.PREFIX)
@Component
@RefreshScope
public class YuFanProperties implements Serializable {
    
    public static final String PREFIX = "externalapi.yf";


    private String baseurl;

    /**
     * token
     */
    private String token;

    /**
     * 项目 guid
     */
    private String projectGuid;



}
