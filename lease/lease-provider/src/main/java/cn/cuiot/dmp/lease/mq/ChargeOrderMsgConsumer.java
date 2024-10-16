package cn.cuiot.dmp.lease.mq;//	模板

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.lease.dto.charge.ChargeOrderPaySuccInsertDto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import cn.cuiot.dmp.lease.service.balance.RechargeNotifyRule;
import cn.cuiot.dmp.lease.service.charge.TbChargeOrderService;
import cn.cuiot.dmp.lease.service.charge.order.ChargePayService;
import cn.cuiot.dmp.pay.service.service.consumer.PayMsgBaseChannel;
import cn.cuiot.dmp.pay.service.service.dto.PayOrderQueryReq;
import cn.cuiot.dmp.pay.service.service.dto.PayOrderQueryResp;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.enums.PayConstant;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import cn.cuiot.dmp.pay.service.service.vo.PaySuccessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Slf4j
public class ChargeOrderMsgConsumer {
    @Autowired
    private ChargePayService chargePayService;
    @Autowired
    private OrderPayAtHandler orderPayAtHandler;
    @Autowired
    private RechargeNotifyRule notifyRule;

    @Autowired
    private TbChargeOrderService chargeOrderService;

    /**
     * 徐雷微信支付回调消息
     *
     * @param paySuccessVO
     */
    @StreamListener(PayMsgBaseChannel.PAYSUCCESSINPUT)
    public void userMessageConsumer(@Payload PaySuccessVO paySuccessVO) {
        log.info("userMessageInput:{}", JsonUtil.writeValueAsString(paySuccessVO));
        if(Objects.equals(paySuccessVO.getBusinessType(), PayConstant.BALANCE)){
            rechargeConsumer(paySuccessVO);
        }else {
            chargeConsumer(paySuccessVO) ;
        }


    }
    public void rechargeConsumer(PaySuccessVO paySuccessVO) {
        log.info("RechargeOrderConsumer-rechargeOrderPayNotify-接收数据:{}", paySuccessVO);
        try {
            notifyRule.payNotify(paySuccessVO);
        } catch (Exception e) {
            log.error("RechargeOrderConsumer-rechargeOrderPayNotify-处理异常:{}", e.getMessage(), e);
        }
    }
    private void chargeConsumer(PaySuccessVO paySuccessVO){
        PayOrderQueryReq payOrderQueryReq = new PayOrderQueryReq();
        TbChargeOrder order = chargeOrderService.getById(paySuccessVO.getOutOrderId());
        AssertUtil.isFalse(order == null, "订单不存在");

        payOrderQueryReq.setOutOrderId(paySuccessVO.getOutOrderId());
        payOrderQueryReq.setOrgId(order.getCompanyId());
        PayOrderQueryResp payOrderQueryResp = orderPayAtHandler.queryOrder(payOrderQueryReq);

        boolean isPaySuccess = Objects.equals(payOrderQueryResp.getStatus(), OrderStatusEnum.PAID.getStatus());
        AssertUtil.isTrue(isPaySuccess, "微信未支付");

        if (isPaySuccess) {
            //支付成功
            ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
            chargeOrderPaySuccInsertDto.setTransactionNo(payOrderQueryResp.getPayOrderId());
            chargeOrderPaySuccInsertDto.setOrderId(Long.valueOf(paySuccessVO.getOutOrderId()));
            chargeOrderPaySuccInsertDto.setPayRate(paySuccessVO.getPayRate());

            chargePayService.paySuccess(chargeOrderPaySuccInsertDto,order);
        } else {
            //支付失败，直接取消订单
            chargePayService.cancelPay(order);
        }
    }
}
