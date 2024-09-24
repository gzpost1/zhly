package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

/**
 * 企业名称DTO
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Data
public class OrganizationRespDTO {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String companyName;
}
