package cn.cuiot.dmp.pay.service.service.vo;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 支付订单查询
 * @author huq
 * @since 2022-06-09
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderQuery extends PageQuery {
    /**
     * 平台父订单号
     */
    private String orderId;
    /**
     * 渠道父订单号
     */
    private String outOrderId;

    /**
     * 支付父订单号
     */
    private String payOrderId;





    /**
     * 支付状态：
     * 1：待支付
     * 2：已取消
     * 4：支付失败
     * 5：已支付
     * 99：其他
     */
    private Byte status;



    /**
     * 商品名称
     */
    private String productName;


    /**
     * 下单开始时间
     */
    private Date payStartDateTime;

    /**
     * 下单结束时间
     */
    private Date payEndDateTime;

    /**
     * 支付渠道id
     */
    private String payChannel;
    /**
     * 支付方式
     * 02:微信支付 03:支付宝 17:云闪付
     */
    private String payMethod;
    /**
     * 平台父订单号列表
     */
    private List<Long> orderIds;
}
