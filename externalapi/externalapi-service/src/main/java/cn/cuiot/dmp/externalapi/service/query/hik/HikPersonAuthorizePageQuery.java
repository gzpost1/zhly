package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 人员授权分页query
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikPersonAuthorizePageQuery extends PageQuery {

    /**
     * 人员id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
