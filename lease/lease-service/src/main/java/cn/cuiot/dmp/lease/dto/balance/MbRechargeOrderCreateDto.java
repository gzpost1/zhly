package cn.cuiot.dmp.lease.dto.balance;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wuyongchong
 * @since 2023-11-30
 */
@Getter
@Setter
public class MbRechargeOrderCreateDto implements Serializable {

    /**
     * 房屋id
     */
    @NotNull(message = "房屋id不能为空")
    private Long houseId;

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空")
    private Integer totalFee;

    /**
     * 用户openId（小程序、公众号支付必填）
     * 支付宝用户id也传该字段
     */
    @NotBlank(message = "openId不能为空")
    private String openId;

    /**
     * 小程序appid
     */
    private String appId;


    /**
     * 企业id(前端不用传 后端获取)
     */
    private Long orgId;



    /**
     * 优惠标记
     */
    private String goodsTag;


    /**
     * 商品简单描述。需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
     * 对应以前的body字段
     * 不能超过20字符
     */
    private String productName;


}
