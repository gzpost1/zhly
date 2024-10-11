package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * app用户信息分页query
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikPersonAppPageQuery extends PageQuery {

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;
}
