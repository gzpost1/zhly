package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import java.util.Date;

/**
 * 收费管理-通知单-发送消息
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@Data
public class ChargeNoticeSendDto {
    /**
     * 缴费管理id
     */
    private Long id;
    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    private String chargeItemName;

    /**
     * 所属账期-开始时间 前端不用传
     */
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间 前端不用传
     */
    private Date ownershipPeriodEnd;
}