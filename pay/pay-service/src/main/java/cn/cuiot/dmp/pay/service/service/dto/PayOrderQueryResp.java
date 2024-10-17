package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huq
 * @ClassName CombineQueryOrderResp
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderQueryResp implements Serializable {
    /**
     * 合单订单号
     */
    private String outOrderId;

    /**
     * 支付状态：
     * 1：待支付
     * 2：已取消
     * 3：支付中
     * 4：支付失败
     * 5：已支付
     * 99：其他
     */
    private Byte status;
    /**
     * 支付总金额
     */
    private Integer totalFee;

    /**
     * 微信订单号
     */
    private String payOrderId;
    /**
     * 支付成功时间
     * yyyy-MM-dd HH:mm:ss
     */
    private Date payCompleteTime;


    /**
     * 二级商户支付号
     */
    private String payMchId;


    /**
     * 1:账单缴费
     * 2：预缴
     */
    private Byte businessType;

    /**
     * 支付手续费费率
     */
    private BigDecimal payRate;




    public static PayOrderQueryResp initReturn(PayOrderQueryAggregate queryBO, SysPayChannelSetting paySetting) {
        return PayOrderQueryResp.builder()
                .outOrderId(queryBO.getOutOrderId())
                .status(queryBO.getStatus())
                .totalFee(queryBO.getTotalFee())
                .payOrderId(queryBO.getPayOrderId())
                .payCompleteTime(queryBO.getPayCompleteTime())
                .payMchId(queryBO.getPayMchId())
                .businessType(StringUtils.isBlank(queryBO.getAttach())?null:Byte.valueOf(queryBO.getAttach()))
                .payRate(paySetting.getCharge())
                .build();
    }
}
