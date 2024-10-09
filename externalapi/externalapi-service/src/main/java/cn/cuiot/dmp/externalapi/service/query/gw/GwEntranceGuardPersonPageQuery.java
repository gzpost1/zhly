package cn.cuiot.dmp.externalapi.service.query.gw;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物门禁 人员管理
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardPersonPageQuery extends PageQuery {

    /**
     * 人员名称
     */
    private String name;

    /**
     * 分组id
     */
    private Long personGroupId;

    /**
     * 授权 0-未授权；1-已授权
     */
    private Byte authorize;
}
