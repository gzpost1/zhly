package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 下单参数
 * @Date 2024/10/10 19:13
 * @Created by libo
 */
@Data
public class ChargePayDto {

    /**
     * 收款项目
     */
    @NotEmpty(message = "收费项目不能为空")
    private List<Long> chargeIds;

    /**
     * 数据类型 0房屋收费 1房屋押金
     */
    @NotNull(message = "数据类型不能为空")
    private Byte dataType;

    /**
     * appId：小程序下单必填
     */
    private String appId;

    /**
     * 用户openId（小程序、公众号支付必填）
     * 支付宝用户id也传该字段
     */
    private String openId;
}
