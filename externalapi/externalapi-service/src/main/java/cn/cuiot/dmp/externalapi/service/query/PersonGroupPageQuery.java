package cn.cuiot.dmp.externalapi.service.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员分组 query
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonGroupPageQuery extends PageQuery {

    /**
     * 分组名称
     */
    private String name;
}
