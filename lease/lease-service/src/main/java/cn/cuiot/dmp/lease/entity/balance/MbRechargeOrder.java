package cn.cuiot.dmp.lease.entity.balance;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.lease.enums.MbRechargeOrderStatus;
import cn.cuiot.dmp.lease.enums.PayStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 充值订单
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_house_recharge_order")
public class MbRechargeOrder extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId("order_id")
    private Long orderId;


    /**
     * 订单金额
     */
    private Integer totalFee;


    /**
     * 支付手续费
     */
    private Integer payCharge;


    /**
     * 支付手续费率
     */
    private BigDecimal payChargeRate;

    /**
     * 支付状态
     */
    private Byte payStatus;


    /**
     * 订单状态
     */
    private Byte status;


    /**
     * 房屋id
     */
    private Long houseId;


    /**
     * 下单时间
     */
    private Date orderTime;




    /**
     * 版本
     */
    private Integer version;


    /**
     * 支付方式
     */
    private Byte payMethod;


    /**
     * 订单上一状态
     */
    private Byte previousStatus;

    /**
     * 小程序appId
     */
    private String appId;
    /**
     * 用户openId
     */
    private String openId;

    /**
     * 微信支付单号
     */
    private String payOrderId;

    public void init(){
        this.orderId = IdWorker.getId();
        this.orderTime = new Date();
        this.status = MbRechargeOrderStatus.TO_BE_PAY.getStatus();
        this.payStatus = PayStatus.P_TO_BE_PAY.getStatus();
        this.version = 1;
    }
    public void updateStatus(MbRechargeOrder old,Byte status,Byte payStatus){
        old.setPreviousStatus(old.getStatus());
        old.setStatus(status);
        old.setPayStatus(payStatus);
        this.setOrderId(old.getOrderId());
        this.setPreviousStatus(old.getPreviousStatus());
        this.setStatus(status);
        this.setPayStatus(payStatus);
        this.setVersion(old.getVersion());
        this.setPayOrderId(old.getPayOrderId());
    }
}
