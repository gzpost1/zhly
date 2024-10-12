package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 附加数据（可作为自定义字段使用，查询API和支付通知中原样返回）
     */
    private String attach;



    public static PayOrderQueryResp initReturn(PayOrderQueryAggregate queryBO) {
        return PayOrderQueryResp.builder()
                .outOrderId(queryBO.getOutOrderId())
                .status(queryBO.getStatus())
                .totalFee(queryBO.getTotalFee())
                .payOrderId(queryBO.getPayOrderId())
                .payCompleteTime(queryBO.getPayCompleteTime())
                .payMchId(queryBO.getPayMchId())
                .attach(queryBO.getAttach())
                .build();
    }
}
