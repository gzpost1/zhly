package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeItemNameSet;
import cn.cuiot.dmp.lease.dto.charge.TransactionModeNameSet;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 收费管理-收银台-缴费明细
 */
@Data
@TableName(value = "tb_charge_received",autoResultMap = true)
public class TbChargeReceived implements ChargeItemNameSet, TransactionModeNameSet {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 收款id
     */
    @TableField(value = "charge_id")
    private Long chargeId;

    /**
     * 本金实收
     */
    @TableField(value = "receivable_amount_received")
    private Integer receivableAmountReceived;

    /**
     * 违约金应收
     */
    @TableField(value = "liquidated_damages_need")
    private Integer liquidatedDamagesNeed;

    /**
     * 违约金实收
     */
    @TableField(value = "liquidated_damages_received")
    private Integer liquidatedDamagesReceived;

    /**
     * 违约金税额
     */
    @TableField(value = "liquidated_damages_tax")
    private Integer liquidatedDamagesTax;

    /**
     * 违约金不含税额
     */
    @TableField(value = "liquidated_damages_not_tax")
    private Integer liquidatedDamagesNotTax;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 实收合计
     */
    @TableField(value = "total_received")
    private Integer totalReceived;

    /**
     * 是否只收本金 0否 1是
     */
    @TableField(value = "only_principal")
    private Byte onlyPrincipal;

    /**
     * 交易方式
     */
    @TableField(value = "transaction_mode")
    private Long transactionMode;

    /**
     * 交易方式名称
     */
    @TableField(exist = false)
    private String transactionModeName;

    /**
     * 入账银行
     */
    @TableField(value = "account_bank")
    private String accountBank;

    /**
     * 入账账号
     */
    @TableField(value = "account_number")
    private String accountNumber;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 交款人
     */
    @TableField(value = "customer_user_id")
    private Long customerUserId;

    /**
     * 欠收合计
     */
    @TableField(value = "total_owe")
    private Integer totalOwe;

    /**
     * 违约金税率
     */
    private BigDecimal liquidatedDamagesRate;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

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

    @TableField(exist = false)
    private String chargeItemName;

    /**
     * 所属账期-开始时间
     */
    @TableField(value = "ownership_period_begin")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @TableField(value = "ownership_period_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    private String customerUserName;

    /**
     * 客户手机号
     */
    @TableField(exist = false)
    private String customerUserPhone;
}