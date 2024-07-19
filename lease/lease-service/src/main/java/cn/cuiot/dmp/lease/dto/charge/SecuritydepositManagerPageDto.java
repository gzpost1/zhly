package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * @Description 押金分页
 * @Date 2024/6/14 10:31
 * @Created by libo
 */
@Data
public class SecuritydepositManagerPageDto implements ChargeItemNameSet,TransactionModeNameSet{
    /**
     *  应收编码/实收编码
     */
    private Long id;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房屋编号
     */
    private String houseCode;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    private String chargeItemName;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    private String transactionModeName;

    /**
     * 入账银行
     */
    private String accountBank;

    /**
     * 入账账号
     */
    private String accountNumber;

    /**
     * 收款时间
     */
    private Date receivedDate;


    /**
     * 备注
     */
    private String remark;

    public Integer getUnreturnedAmount() {
        if(Objects.equals(status, SecurityDepositStatusEnum.UNPAID.getCode())){
            return 0;
        }
        return receivableAmount - returnedAmount;
    }
}
