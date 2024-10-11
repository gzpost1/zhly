package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源列表query
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikIrdsResourcesByParamsQuery extends PageQuery {

    /**
     * 资源名称
     */
    private String name;
}
