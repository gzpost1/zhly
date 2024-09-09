package cn.cuiot.dmp.externalapi.service.query;

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
     * json数据
     */
    private String data;

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 企业id
     */
    private String companyId;
}
