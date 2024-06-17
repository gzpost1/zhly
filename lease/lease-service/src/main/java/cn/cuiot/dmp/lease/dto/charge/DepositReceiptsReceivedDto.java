package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 收款
 * @Date 2024/6/13 9:09
 * @Created by libo
 */
@Data
public class DepositReceiptsReceivedDto {
    /**
     * 应收id
     */
    private Long chargeId;

    /**
     * 交易方式
     */
    @NotNull(message = "交易方式不能为空")
    private Long transactionMode;

    /**
     * 入账银行
     */
    @NotBlank(message = "入账银行不能为空")
    private String accountBank;

    /**
     * 入账账号
     */
    @NotBlank(message = "入账账号不能为空")
    private String accountNumber;
}
