package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/7/18 20:48
 */
@Data
public class PlatFromDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * app_key
     */
    private String appKey;

    /**
     * app_secret
     */
    private String appSecret;

    /**
     * project_guid
     */
    private String projectGuid;
}
