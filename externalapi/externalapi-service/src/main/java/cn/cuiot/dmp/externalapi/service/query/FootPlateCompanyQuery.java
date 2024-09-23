package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/7/22 14:29
 */
@Data
public class FootPlateCompanyQuery {

    /**
     * 平台id
     */
    private  Long platformId;

    /**
     * 企业id
     */
    private Long  companyId;
}
