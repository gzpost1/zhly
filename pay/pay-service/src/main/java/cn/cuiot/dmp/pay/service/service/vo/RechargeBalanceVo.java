package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 余额变动任务参数
 *
 * @author huq
 * @ClassName MbBalanceEventDto
 * @Date 2023/11/17 14:44
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RechargeBalanceVo implements Serializable {


    /**
     * 变动的余额
     */
    @Min(value = 0,message = "充值金额不能小于0")
    @NotNull(message = "充值金额不能为空")
    private Integer balance;


    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;

    /**
     * 原因
     */
    @Length(max = 200,message = "最长不能超过200")
    private String reason;

}
