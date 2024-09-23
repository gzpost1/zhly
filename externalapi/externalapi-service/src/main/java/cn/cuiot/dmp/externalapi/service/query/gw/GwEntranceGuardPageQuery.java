package cn.cuiot.dmp.externalapi.service.query.gw;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物门禁分页 query
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardPageQuery extends PageQuery {

    /**
     * 门禁名称
     */
    private String name;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 门禁SN
     */
    private String sn;

    /**
     * 门禁品牌id
     */
    private Long brandId;

    /**
     * 门禁型号id
     */
    private Long modelId;

    /**
     * 启停用状态 1-启用 0-停用
     */
    private Byte status;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）
     */
    private String equipStatus;
}
