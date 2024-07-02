package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 押金退款
 * @Date 2024/6/17 10:55
 * @Created by libo
 */
@Data
public class SecuritydepositRefundDto {
    /**
     * 退款金额
     */
    private Integer refundAmount;
    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款方式
     */
    private Long refundMode;

    /**
     * 退款银行
     */
    private String refundBank;

    /**
     * 退款账号
     */
    private String refundNumber;

    /**
     * 押金id
     */
    private Long depositId;
}
