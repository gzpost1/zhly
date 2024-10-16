package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 修改余额类型
     * 0-增加，1-减少
     */
    @NotNull(message = "修改余额类型不能为空")
    private Byte changeFlag;

    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    private Integer balance;
    /**
     * 用户手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 验证码，减少金额时必传
     */
    private String smsCode;

    public void valid() {
        if (EntityConstants.YES.equals(this.changeFlag) && StringUtils.isEmpty(this.smsCode)) {
            throw  new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "扣减用户余额时验证码必传");
        }

    }
}
