package cn.cuiot.dmp.pay.service.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 结算报表
 */
@Data
@TableName(value = "tb_order_settlement")
public class TbOrderSettlement {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 应收编码
     */
    @TableField(value = "receivable_id")
    private Long receivableId;

    /**
     * 实收编码
     */
    @TableField(value = "paid_up_id")
    private Long paidUpId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 房屋id
     */
    @TableField(value = "house_id")
    private Long houseId;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 交易渠道 0平台 1人工
     */
    @TableField(value = "payment_mode")
    private Byte paymentMode;

    /**
     * 交易单号
     */
    @TableField(value = "transaction_no")
    private String transactionNo;

    /**
     * 交易订单号
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 收支类型 0收入 1支出
     */
    @TableField(value = "income_type")
    private Byte incomeType;

    /**
     * 收费项目id
     */
    @TableField(value = "charge_item_id")
    private Long chargeItemId;

    /**
     * 支出项目 0支付手续费
     */
    @TableField(value = "expenditure_type")
    private Byte expenditureType;

    /**
     * 交易方式
     */
    @TableField(value = "transaction_mode")
    private Long transactionMode;

    /**
     * 金额
     */
    @TableField(value = "pay_amount")
    private Integer payAmount;

    /**
     * 结算时间
     */
    @TableField(value = "settlement_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date settlementTime;

    /**
     * 所属楼盘id
     */
    @TableField(value = "loupan_id")
    private Long loupanId;

    /**
     * 房屋名称
     */
    @TableField(exist = false)
    private String houseName;

    /**
     * 房屋编号
     */
    @TableField(exist = false)
    private String houseCode;

    /**
     * 收费项目名称
     */
    @TableField(exist = false)
    private String chargeItemName;

    /**
     * 楼盘名称
     */
    @TableField(exist = false)
    private String loupanName;

    /**
     * 交易方式名称
     */
    @TableField(exist = false)
    private String transactionModeName;


}