package cn.cuiot.dmp.upload.infrastructure.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: wuyongchong
 * @date: 2023/10/12 11:33
 */
@Data
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {
    /**
     * 配置前缀
     */
    public static final String PREFIX = "oss";

    /**
     * OSS平台
     */
    private String platform;

    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * Access key
     */
    private String accessKey;

    /**
     * Secret key
     */
    private String secretKey;

    /**
     * 区域
     */
    private String region;

    /**
     * 默认的存储桶名称
     */
    private String bucketName;

    /**
     * 预签url有效期(默认一天)-单位秒
     */
    private Long presignedExpire;

    /**
     * 访问域名
     */
    private String domainUrl;

    /**
     * 访问域名
     */
    private String tmpDirPath;

    /**
     * 是否压缩图片 1 是 0否
     */
    private Byte compressImg;

    /**
     * 禁止上传文件后缀
     */
    private List<String> forbiddenSuffix;

    /**
     * 允许上传文件目录
     */
    private List<String> allowDirs;
}
