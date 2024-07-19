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
     * 序号
     */
    private Integer sort;


    /**
     * 平台类型
     */
    private String platfromType;

    /**
     * 平台名称
     */
    private String platfromName;

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
