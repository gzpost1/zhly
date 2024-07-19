package cn.cuiot.dmp.system.infrastructure.entity.dto;

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
