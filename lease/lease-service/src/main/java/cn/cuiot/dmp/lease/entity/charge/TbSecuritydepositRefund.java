package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.lease.dto.charge.RefundModeNameSet;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 收费管理-收银台-押金管理-退款
 */
@Data
@TableName(value = "tb_securitydeposit_refund")
public class TbSecuritydepositRefund implements RefundModeNameSet {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 退款金额
     */
    @TableField(value = "refund_amount")
    private Integer refundAmount;

    /**
     * 退款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "refund_time")
    private Date refundTime;

    /**
     * 退款原因
     */
    @TableField(value = "refund_reason")
    private String refundReason;

    /**
     * 退款方式
     */
    @TableField(value = "refund_mode")
    private Long refundMode;


    /**
     * 退款方式
     */
    @TableField(exist = false)
    private String refundModeName;


    /**
     * 退款银行
     */
    @TableField(value = "refund_bank")
    private String refundBank;

    /**
     * 退款账号
     */
    @TableField(value = "refund_number")
    private String refundNumber;

    /**
     * 押金id
     */
    private Long depositId;
}