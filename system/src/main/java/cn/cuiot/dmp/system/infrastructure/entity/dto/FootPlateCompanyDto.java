package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/7/22 14:31
 */
@Data
public class FootPlateCompanyDto {

    /**
     * id
     */
    private Long id;
    /**
     * appKey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * projectGuid
     */
    private String projectGuid;

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 企业id
     */
    private String companyId;
}
