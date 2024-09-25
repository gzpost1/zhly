package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/7/18 18:01
 */
@Data
public class PortraitInputInfoDto {

    /**
     * AppKey
     */
    private String appKey;

    /**
     * AppSecret
     */
    private String appSecret;

    /**
     * projectGuid
     */
    private String projectGuid;
}
