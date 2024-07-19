package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import java.util.Date;

/**
 * 收费管理-收银台-押金管理
 */
@Data
public class SecuritydepositManagerInsertDto {

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费标准 0自定义金额
     */
    private Byte chargeStandard;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    /**
     * 所属账期-开始时间
     */
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    private Date ownershipPeriodEnd;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 应收日期
     */
    private Date dueDate;

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 备注
     */
    private String remark;
}