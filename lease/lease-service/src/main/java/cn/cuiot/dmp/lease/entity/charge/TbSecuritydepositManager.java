package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 收费管理-收银台-押金管理
 */
@Data
@TableName(value = "tb_securitydeposit_manager")
public class TbSecuritydepositManager {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "customer_user_id")
    private Long customerUserId;

    /**
     * 房屋id
     */
    @TableField(value = "house_id")
    private Long houseId;

    /**
     * 收费项目id
     */
    @TableField(value = "charge_item_id")
    private Long chargeItemId;

    /**
     * 收费标准 0自定义金额
     */
    @TableField(value = "charge_standard")
    private Byte chargeStandard;

    /**
     * 应收金额/本金
     */
    @TableField(value = "receivable_amount")
    private Integer receivableAmount;

    /**
     * 所属账期-开始时间
     */
    @TableField(value = "ownership_period_begin")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @TableField(value = "ownership_period_end")
    private Date ownershipPeriodEnd;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 是否删除 0否 1是
     */
    @TableField(value = "deleted")
    private Byte deleted;

    /**
     * 应收日期
     */
    @TableField(value = "due_date")
    private Date dueDate;

    /**
     * 状态 0未交款 1已交清 2未退完 3 已退完 4作废
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 本金实收
     */
    @TableField(value = "receivable_amount_received")
    private Integer receivableAmountReceived;

    /**
     * 已退金额
     */
    @TableField(value = "returned_amount")
    private Integer returnedAmount;
}