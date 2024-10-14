package cn.cuiot.dmp.lease.service.balance;

import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.balance.MbRechargeOrderCreateDto;
import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.enums.MbRechargeOrderStatus;
import cn.cuiot.dmp.lease.enums.PayStatus;
import cn.cuiot.dmp.lease.rocketmq.IMqMemberConsumerConfig;
import cn.cuiot.dmp.lease.vo.balance.MbRechargeOrderCreateVo;
import cn.cuiot.dmp.pay.service.service.dto.*;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.enums.TradeChannelEnum;
import cn.cuiot.dmp.pay.service.service.service.BalanceRuleAtHandler;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import com.xxl.job.core.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import static cn.cuiot.dmp.lease.constants.Constants.PLATFORM;
import static cn.cuiot.dmp.lease.constants.Constants.RECHARGE_ORDER_NOTIFY;
import static cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum.BALANCE_RECHARGE;

/**
 * 充值订单支付业务
 *
 * @author huq
 * @ClassName RechargeOrderPayRule
 * @Date 2023/11/30 14:14
 **/
@Slf4j
@Service
public class RechargeOrderPayRule {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private OrderPayAtHandler orderPayAtHandler;

    @Autowired
    private MbRechargeOrderService orderService;
    @Autowired
    private MbRechargeOrderChangeService changeService;
    @Autowired
    private StreamMessageSender streamMessageSender;
    @Autowired
    private BalanceRuleAtHandler balanceRuleAtHandler;
    /**
     * 下单接口
     *
     * @param param
     * @return
     */
    public MbRechargeOrderCreateVo createOrder(MbRechargeOrderCreateDto param, MbRechargeOrder order) {
        //基础信息填充
        order.init();
        //2023/11/30 调用支付
        MbRechargeOrderCreateVo resp = toPay(order, param);
        // 入库并组装返回
        createToSql(order);
        //丢超时处理队列，5分钟够了
        streamMessageSender.sendGenericMessage(
                IMqMemberConsumerConfig.MB_RECHARGE_ORDER_MESSAGE_INPUT,
                SimpleMsg.builder()
                        .delayTimeLevel(9)
                        .data(order.getOrderId())
                        .info("余额充值超时")
                        .build());
        return resp;
    }

    /**
     * 调用支付
     *
     * @param order
     * @return
     */
    private MbRechargeOrderCreateVo toPay(MbRechargeOrder order, MbRechargeOrderCreateDto param) {

        CreateOrderReq createOrderDto = new CreateOrderReq();
        createOrderDto.setOrgId(orderService.quertOrgIdByHouse(param.getHouseId()));
        createOrderDto.setOutOrderId(String.valueOf(order.getOrderId()));
        createOrderDto.setPayChannel(PayChannelEnum.WECHAT_NORMAL.getPayChannel());
        createOrderDto.setMchType(PayChannelEnum.WECHAT_NORMAL.getMchType());
        createOrderDto.setTradeType(TradeChannelEnum.MINI_APP.getPayCode());
        createOrderDto.setOpenId(param.getOpenId());
        createOrderDto.setTotalFee(param.getTotalFee());
        createOrderDto.setSpbillCreateIp(IpUtil.getIp());
        createOrderDto.setGoodsTag(param.getGoodsTag());
        createOrderDto.setProductName(param.getProductName());
        createOrderDto.setBusinessType((byte)2);
        CreateOrderResp createOrderResp = orderPayAtHandler.makeOrder(createOrderDto);

        MbRechargeOrderCreateVo resp = MbRechargeOrderCreateVo.builder()
                .orderId(order.getOrderId())
                .payContent(createOrderResp)
                .build();
        return resp;
    }

    /**
     * 创建订单并入库
     *
     * @param order
     */
    private void createToSql(MbRechargeOrder order) {

        TransactionStatus transaction = null;
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setTimeout(30);
            transaction = transactionManager.getTransaction(definition);
            orderService.save(order);
            // 2023/11/30 状态变更
            changeService.orderChange(order, PLATFORM);
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("数据库入库异常", ex);
            throw new BusinessException(ResultCode.SERVER_BUSY, ex.getMessage());
        }

    }

    public void payOvertimeHandler(Long orderId) {
        MbRechargeOrder order = orderService.getById(orderId);
        if (order == null || MbRechargeOrderStatus.paid(order.getStatus())) {
            return;
        }
        payOvertimeHandler(order);
    }

    /**
     * 超时订单处理，查支付结果，如果支付成功则进行充值，未支付则关单
     *
     * @param order
     */
    public void payOvertimeHandler(MbRechargeOrder order) {

        PayOrderQueryReq orderDto = new PayOrderQueryReq();
        orderDto.setOutOrderId(order.getOrderId().toString());
        PayOrderQueryResp payOrderQueryResp = orderPayAtHandler.queryOrder(orderDto);
        boolean isPaySuccess = Objects.equals(payOrderQueryResp.getStatus(), OrderStatusEnum.PAID.getStatus());
        if (isPaySuccess) {
            // 调支付成功处理
            BigDecimal payRate = payOrderQueryResp.getPayRate();
            order.setPayChargeRate(payOrderQueryResp.getPayRate());
            order.setPayCharge(new BigDecimal(order.getTotalFee()).multiply(payRate).divide(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
            paySuccessHandler(order);
            return;
        }
        CloseOrderReq param = new CloseOrderReq();
        param.setOutOrderId(order.getOrderId().toString());
        log.error("关闭订单，订单详细：{}", order);
        orderPayAtHandler.closeOrder(param);
        payFailHandler(order, MbRechargeOrderStatus.CANCEL.getStatus());

    }

    /**
     * 支付成功
     *
     * @param order
     */
    public void paySuccessHandler(MbRechargeOrder order) {
        MbRechargeOrder updateOrder = new MbRechargeOrder();
        updateOrder.updateStatus(order, MbRechargeOrderStatus.RECHARGING.getStatus(), PayStatus.P_PAID.getStatus());
        updateOrder.setPayCharge(order.getPayCharge());
        updateOrder.setPayChargeRate(order.getPayChargeRate());
        //先变更为支付成功充值中，再调用余额充值，如果余额充值失败，则发mq重试，重试还是失败则发起退款
        updateToSql(updateOrder);
        order.setVersion(updateOrder.getVersion() + 1);
        Boolean flag = recharge(order);
        if (!flag) {
            throw new  BusinessException(ResultCode.SERVER_BUSY, "充值失败");
        }

    }

    /**
     * 充值并变更订单状态，返回是否成功
     *
     * @param order
     */
    private Boolean recharge(MbRechargeOrder order) {
        TransactionStatus transaction = null;
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setTimeout(30);
            transaction = transactionManager.getTransaction(definition);
            MbRechargeOrder updateOrder = new MbRechargeOrder();
            updateOrder.updateStatus(order, MbRechargeOrderStatus.RECHARGING_TO_ACCOUNT.getStatus(),
                    PayStatus.P_PAID.getStatus());

            BalanceEventAggregate balanceEventAggregate = BalanceEventAggregate.builder()
                    .balance(order.getTotalFee())
                    .changeType(BALANCE_RECHARGE.getType())
                    .createTime(new Date())
                    .orderId(order.getOrderId().toString())
                    .orderName("余额充值")
                    .houseId(order.getHouseId())
                    .build();
            orderService.updateOrder(updateOrder);
            // 2023/11/30 状态变更
            changeService.orderChange(updateOrder, PLATFORM);
            IdmResDTO handler = balanceRuleAtHandler.handler(balanceEventAggregate);
            if (!ErrorCode.SUCCESS.getCode().equals(handler.getCode())) {
                throw new BusinessException(handler.getCode(), handler.getMessage());
            }
            transactionManager.commit(transaction);
            return true;
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("余额充值发生异常，等待重试", ex);
            return false;
        }
    }
    /**
     * 订单变更并入库
     */
    private void updateToSql(MbRechargeOrder updateOrder) {
        TransactionStatus transaction = null;
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setTimeout(30);
            transaction = transactionManager.getTransaction(definition);
            orderService.updateOrder(updateOrder);
            // 2023/11/30 状态变更
            changeService.orderChange(updateOrder, PLATFORM);
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("数据库入库异常", ex);
            throw new BusinessException(ResultCode.SERVER_BUSY, ex.getMessage());
        }
    }
    /**
     * 支付失败或已取消
     *
     * @param order
     */
    public void payFailHandler(MbRechargeOrder order, Byte status) {
        MbRechargeOrder updateOrder = new MbRechargeOrder();
        updateOrder.updateStatus(order, status, MbRechargeOrderStatus.CANCEL.getStatus() == status.byteValue() ?
                PayStatus.P_CANCEL.getStatus() : PayStatus.P_PAY_FAILED.getStatus());
        updateToSql(updateOrder);
    }
}
