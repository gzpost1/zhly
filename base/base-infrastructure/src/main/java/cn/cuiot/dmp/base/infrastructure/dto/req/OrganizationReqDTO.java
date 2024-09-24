package cn.cuiot.dmp.base.infrastructure.dto.req;

import cn.cuiot.dmp.domain.types.id.OrganizationId;
import lombok.Data;

import java.util.List;

/**
 * 企业查询DTO
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Data
public class OrganizationReqDTO {

    /**
     * id列表
     */
    private List<Long> idList;

    /**
     * 企业名称
     */
    private String companyName;
}
