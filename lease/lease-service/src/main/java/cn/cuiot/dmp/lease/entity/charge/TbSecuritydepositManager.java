package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * 收费管理-收银台-押金管理
 */
@Data
@TableName(value = "tb_securitydeposit_manager")
public class TbSecuritydepositManager extends YjBaseEntity {
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
    private Long chargeStandard;

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

    /**
     * 交易方式
     */
    @TableField(value = "transaction_mode")
    private Long transactionMode;

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
     * 收款时间
     */
    private Date receivedDate;

    /**
     * 所属楼盘id
     */
    private Long loupanId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 实收编码
     *
     */
    private Long receivedId;

    /**
     * 收款方式 0平台 1人工
     */
    private Byte paymentMode;

    /**
     * 交易单号
     */
    private String transactionNo;

    /**
     * 缴费时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastPayTime;

    /**
     * 支付状态 0待支付 1支付取消 2支付失败 3支付成功
     */
    private Byte payStatus;

    /**
     * 某次下单使用的订单号
     */
    private Long orderId;
}