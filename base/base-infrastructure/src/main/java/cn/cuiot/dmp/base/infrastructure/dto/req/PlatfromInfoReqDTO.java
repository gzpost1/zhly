package cn.cuiot.dmp.base.infrastructure.dto.req;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对接外部api dto
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlatfromInfoReqDTO extends PageQuery {

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 公司id
     */
    private Long companyId;
}
