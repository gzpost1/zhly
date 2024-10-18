package cn.cuiot.dmp.lease.dto.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @Description 押金分页
 * @Date 2024/6/14 10:31
 * @Created by libo
 */
@Data
public class SecuritydepositManagerPageDto implements ChargeItemNameSet,TransactionModeNameSet,ChargeStandardNameSet{
    /**
     *  应收编码/实收编码
     */
    @Excel(name = "押金实收编码", orderNum = "0", width = 20)
    private Long id;

    @Excel(name = "押金应收编码", orderNum = "1", width = 20)
    private Long receivableId;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", orderNum = "4", width = 20)
    private String customerUserName;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "2", width = 20)
    private String houseName;

    /**
     * 房屋编号
     */
    @Excel(name = "房屋编码", orderNum = "3", width = 20)
    private String houseCode;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "5", width = 20)
    private String chargeItemName;

    /**
     * 收费标准 0自定义金额
     */
    private Long chargeStandard;

    /**
     * 收费标准 0自定义金额
     */
    private String chargeStandardName;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-开始时间",orderNum = "6",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-结束时间",orderNum = "7",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 应收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dueDate;

    /**
     * 状态 0未交款 1已交清 2未退完 3 已退完 4作废
     */
    private Byte status;

    /**
     * 本金实收
     */
    private Integer receivableAmountReceived;

    @Excel(name = "实收金额", orderNum = "8", width = 20)
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
     * 已退金额
     */
    private Integer returnedAmount;

    /**
     * 未退金额
     */
    private Integer unreturnedAmount;

    /**
     * 交易方式
     */
    private Long transactionMode;

    /**
     * 交易方式名称
     */
    @Excel(name = "交易方式", orderNum = "12", width = 20)
    private String transactionModeName;

    /**
     * 入账银行
     */
    @Excel(name = "入账银行", orderNum = "9", width = 20)
    private String accountBank;

    /**
     * 入账账号
     */
    @Excel(name = "入账账号", orderNum = "10", width = 20)
    private String accountNumber;

    /**
     * 收款时间
     */
    private Date receivedDate;

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

    @Excel(name = "收款方式", orderNum = "11", width = 20,replace = {"人工_1", "平台_0"})
    private String paymentModeName;

    /**
     * 交易单号
     */
    @Excel(name = "交易单号", orderNum = "13", width = 20)
    private String transactionNo;
    public Integer getUnreturnedAmount() {
        if(Lists.newArrayList(SecurityDepositStatusEnum.UNPAID.getCode(),SecurityDepositStatusEnum.CANCELLED.getCode()).contains(status)){
            return 0;
        }
        return receivableAmount - returnedAmount;
    }
    public Long getReceivableId(){
        return id;
    }
}
