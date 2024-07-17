package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * 收费管理-催款管理发送消息Dto
 *
 * @Author: zc
 * @Date: 2024-06-24
 */
@Data
public class ChargeCollectionManageSendDto {
    /**
     * 费用条数
     */
    private String total;

    /**
     * 欠款金额
     */
    private String amount;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 企业ID
     */
    private Long buildingId;
}