package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 收费管理-通知单分页query
 *
 * @Author: zc
 * @Date: 2024-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeNoticePageQuery extends PageQuery {

    /**
     * 通知单号
     */
    private Long id;

    /**
     * 楼盘id列表
     */
    private List<Long> buildings;

    /**
     * 收费项目列表
     */
    private List<Long> chargeItems;

    /**
     * 企业ID
     */
    private Long companyId;
}