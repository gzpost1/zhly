package cn.cuiot.dmp.lease.service.balance;

import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.enums.MbRechargeOrderStatus;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.vo.PaySuccessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static cn.cuiot.dmp.lease.enums.ChargeReceivbleEnum.PAID;

/**
 * 订单通知业务处理
 * 支付或退款通知入口都在这儿
 *
 * @author huq
 * @ClassName RechargeNotifyRule
 * @Date 2023/11/30 15:30
 **/
@Service
@Slf4j
public class RechargeNotifyRule {
    @Autowired
    private MbRechargeOrderService orderService;
    @Autowired
    private RechargeOrderPayRule orderPayRule;

    /**
     * 支付通知
     *
     * @param paySuccessVO
     */
    public void payNotify(PaySuccessVO paySuccessVO) {
        MbRechargeOrder rechargeOrder = orderService.getById(Long.parseLong(paySuccessVO.getOutOrderId()));
        if (null == rechargeOrder) {
            log.warn("订单【{}】不存在", paySuccessVO.getOutOrderId());
            return;
        }
        if (!MbRechargeOrderStatus.canPayNotify(rechargeOrder.getStatus())) {
            log.warn("订单状态已经过了处理支付通知范围，订单号【{}】",paySuccessVO.getOutOrderId());
            return;
        }
        if (paySuccess(paySuccessVO)) {
            BigDecimal payRate = paySuccessVO.getPayRate();
            rechargeOrder.setPayChargeRate(paySuccessVO.getPayRate());
            rechargeOrder.setPayCharge(new BigDecimal(rechargeOrder.getTotalFee()).multiply(payRate).divide(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
            rechargeOrder.setPayOrderId(paySuccessVO.getTransactionNo());
            orderPayRule.paySuccessHandler(rechargeOrder);
        } else {
            orderPayRule.payFailHandler(rechargeOrder, MbRechargeOrderStatus.PAY_FAILED.getStatus());
        }
    }

    public boolean paySuccess(PaySuccessVO paySuccessVO) {
        return Objects.nonNull(paySuccessVO) && Objects.equals(OrderStatusEnum.PAID.getStatus(),paySuccessVO.getStatus());
    }

}
