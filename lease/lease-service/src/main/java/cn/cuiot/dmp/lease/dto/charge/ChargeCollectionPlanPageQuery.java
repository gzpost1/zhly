package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 收费管理-催款计划 分页query
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionPlanPageQuery extends PageQuery {
    /**
     * 催款计划id
     */
    private Long id;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 通知渠道（1:短信，2:微信）
     */
    private Byte channel;

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