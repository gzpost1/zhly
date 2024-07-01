package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import java.util.List;

/**
 * 收费管理-缴费管理-缴费记录统计query
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@Data
public class ChargeCollectionRecordStatisticsQuery {
    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 客户ID列表
     */
    private List<Long> customerUserIds;
}