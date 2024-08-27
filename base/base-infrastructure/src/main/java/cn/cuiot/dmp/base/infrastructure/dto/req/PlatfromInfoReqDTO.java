package cn.cuiot.dmp.base.infrastructure.dto.req;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.*;

/**
 * 对接外部api dto
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PlatfromInfoReqDTO extends PageQuery {

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 公司id
     */
    private Long companyId;
}
