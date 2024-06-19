package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费管理-收银台-缴费管理
 */
@Data
@TableName(value = "tb_charge_manager", autoResultMap = true)
public class TbChargeManager extends YjBaseEntity {
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
     * 收费方式 0自然月周期 1临时收费
     */
    @TableField(value = "charge_type")
    private Byte chargeType;

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
     * 应收日期
     */
    @TableField(value = "due_date")
    private Date dueDate;

    /**
     * 所属类型 0手动创建  1自动生成
     */
    @TableField(value = "create_type")
    private Byte createType;

    /**
     * 自动生成计划编码
     */
    @TableField(value = "receivble_plan_id")
    private Long receivblePlanId;

    /**
     * 应收状态 0未交款 1已交款 2已交清
     */
    @TableField(value = "receivble_status")
    private Byte receivbleStatus;

    /**
     * 挂起状态 0未挂起 1已挂起
     */
    @TableField(value = "hang_up_status")
    private Byte hangUpStatus;

    /**
     * 作废状态 0正常 1已作废
     */
    @TableField(value = "abrogate_status")
    private Byte abrogateStatus;

    /**
     * 本金税率
     */
    @TableField(value = "receivable_amount_rate")
    private BigDecimal receivableAmountRate;

    /**
     * 本金实收
     */
    @TableField(value = "receivable_amount_received")
    private Integer receivableAmountReceived = 0;

    /**
     * 违约金应收
     */
    @TableField(value = "liquidated_damages_need")
    private Integer liquidatedDamagesNeed = 0;

    /**
     * 违约金实收
     */
    @TableField(value = "liquidated_damages_received")
    private Integer liquidatedDamagesReceived = 0;

    /**
     * 违约金税额
     */
    @TableField(value = "liquidated_damages_tax")
    private Integer liquidatedDamagesTax = 0;

    /**
     * 违约金不含税额
     */
    @TableField(value = "liquidated_damages_not_tax")
    private Integer liquidatedDamagesNotTax = 0;
}