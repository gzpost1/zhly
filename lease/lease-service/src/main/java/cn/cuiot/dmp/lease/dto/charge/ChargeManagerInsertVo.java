package cn.cuiot.dmp.lease.dto.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费管理-收银台-缴费管理
 */
@Data
public class ChargeManagerInsertVo {

    /**
     * 客户id
     */
    @NotNull(message = "客户id不能为空")
    private Long customerUserId;

    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;

    /**
     * 收费项目id
     */
    @NotNull(message = "收费项目id不能为空")
    private Long chargeItemId;

    /**
     * 收费标准 0自定义金额
     */
    @NotNull(message = "收费标准不能为空")
    private Byte chargeStandard;

    /**
     * 应收金额/本金
     */
    @NotNull(message = "应收金额不能为空")
    private Integer receivableAmount;

    /**
     * 收费方式 0自然月周期 1临时收费
     */
    @NotNull(message = "收费方式不能为空")
    private Byte chargeType;

    /**
     * 所属账期-开始时间
     */
    @NotNull(message = "所属账期-开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @NotNull(message = "所属账期-结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 应收账期月的第几日
     */
    private Integer dueDateNum;

    /**
     * 应收日期-指定日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dueDate;

    /**
     * 本金税率
     */
    private BigDecimal receivableAmountRate;

    /**
     * 企业ID
     */
    private Long companyId;
}