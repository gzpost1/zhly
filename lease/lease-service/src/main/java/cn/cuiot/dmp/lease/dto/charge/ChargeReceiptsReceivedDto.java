package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 收款
 * @Date 2024/6/13 9:09
 * @Created by libo
 */
@Data
public class ChargeReceiptsReceivedDto {
    /**
     * 是否只收本金 0否 1是
     */
    @NotNull(message = "是否只收本金不能为空")
    private Byte onlyPrincipal;

    /**
     * 交易方式 0微信支付 1预缴代扣
     */
    private Long transactionMode;

    /**
     * 入账银行
     */
    private String accountBank;

    /**
     * 入账账号
     */
    private String accountNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 交款人
     */
    private Long customerUserId;

    /**
     * 交款明细
     */
    @Valid
    @NotEmpty(message = "交款明细不能为空")
    private List<ChargeReceiptsReceivedInsertDetailDto> receivedList;
}
