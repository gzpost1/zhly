package cn.cuiot.dmp.lease.entity.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.lease.dto.charge.ChargeItemNameSet;
import cn.cuiot.dmp.lease.dto.charge.ChargeStandardNameSet;
import cn.cuiot.dmp.lease.dto.charge.TransactionModeNameSet;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 收费管理-收银台-缴费明细
 */
@Data
@TableName(value = "tb_charge_received",autoResultMap = true)
public class TbChargeReceived implements ChargeItemNameSet, TransactionModeNameSet, ChargeStandardNameSet {
    /**
     * 实收id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Excel(name = "实收编码", orderNum = "1", width = 20)
    private Long id;

    /**
     * 应收id
     */
    @TableField(value = "charge_id")
    @Excel(name = "应收编码", orderNum = "0", width = 20)
    private Long chargeId;

    /**
     * 本金实收
     */
    @TableField(value = "receivable_amount_received")
    private Integer receivableAmountReceived;


    @Excel(name = "实收本金", orderNum = "8", width = 20)
    @TableField(exist = false)
    private String receivableAmountReceivedName;

    public String getReceivableAmountReceivedName(){
        Double num = receivableAmountReceived / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }
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

    @Excel(name = "实收违约金", orderNum = "9", width = 20)
    @TableField(exist = false)
    private String liquidatedDamagesReceivedName;

    public String getLiquidatedDamagesReceivedName(){
        Double num = liquidatedDamagesReceived/ 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }



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
    @Excel(name = "创建时间",orderNum = "13",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
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

    @Excel(name = "实收合计", orderNum = "7", width = 20)
    @TableField(exist = false)
    private String totalReceivedName;

    public String getTotalReceivedName(){
        Double num = totalReceived / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        return formattedAmount;
    }

    /**
     * 是否只收本金 0否 1是
     */
    @TableField(value = "only_principal")
    private Byte onlyPrincipal;

    /**
     * 交易方式 0微信支付 1预缴代扣
     */
    @TableField(value = "transaction_mode")
    private Long transactionMode;

    /**
     * 交易方式名称
     */
    @TableField(exist = false)
    @Excel(name = "交易方式", orderNum = "10", width = 20)
    private String transactionModeName;

    /**
     * 入账银行
     */
    @TableField(value = "account_bank")
    @Excel(name = "入账银行", orderNum = "11", width = 20)
    private String accountBank;

    /**
     * 入账账号
     */
    @TableField(value = "account_number")
    @Excel(name = "入账账号", orderNum = "12", width = 20)
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
    @Excel(name = "收费项目", orderNum = "4", width = 20)
    private String chargeItemName;

    /**
     * 所属账期-开始时间
     */
    @TableField(value = "ownership_period_begin")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-开始时间",orderNum = "5",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @TableField(value = "ownership_period_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-结束时间",orderNum = "6",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    @Excel(name = "客户名称", orderNum = "3", width = 20)
    private String customerUserName;

    /**
     * 客户手机号
     */
    @TableField(exist = false)
    private String customerUserPhone;

    /**
     * 收款方式 0平台 1人工
     */
    private Byte paymentMode;

    @Excel(name = "收费方式", width = 20, orderNum = "15", replace = {"人工_1", "平台_0"})
    @TableField(exist = false)
    private String paymentModeName;

    /**
     * 交易单号
     */
    @Excel(name = "交易单号", width = 20, orderNum = "16")
    private String transactionNo;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 收费标准 0自定义金额
     */
    @TableField(value = "charge_standard")
    private Long chargeStandard;

    /**
     * 收费标准 0自定义金额
     */
    @Excel(name = "收费标准", orderNum = "14", width = 20)
    @TableField(exist = false)
    private String chargeStandardName;
}