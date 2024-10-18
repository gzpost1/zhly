package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后台操作余额
 *
 * @author huq
 * @ClassName MbBalanceChangeDto
 * @Date 2023/11/17 14:44
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeDto implements Serializable {


    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;
    /**
     * 金额
     */
    @Max(value = 10000000,message = "最大值为100000")
    @Min(value = -10000000,message = "最大值为-100000")
    @NotNull(message = "金额不能为空")
    private Integer balance;

    /**
     * 原因
     */
    @Length(max = 200,message = "最长不能超过200")
    private String reason;

    public void valid() {
        if (0== balance) {
            new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "余额充值或扣减金额不能为0");
        }

    }
}
