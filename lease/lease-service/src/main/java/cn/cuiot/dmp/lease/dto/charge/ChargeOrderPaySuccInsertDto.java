package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 支付业务处理
 * @Date 2024/10/11 11:53
 * @Created by libo
 */
@Data
public class ChargeOrderPaySuccInsertDto {
    /**
     * 微信交易单号
     */
    private String transactionNo;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 数据id
     */
    private List<Long> dataIds;

    /**
     * 下单环境
     */
    private TbChargeOrder order;

    /**
     * 交易方式 0微信支付 1预缴代扣
     */
    private Long transactionMode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款方式 0平台 1人工
     */
    private Byte paymentMode;

    /**
     * 支付手续费费率
     */
    private BigDecimal payRate;
}
