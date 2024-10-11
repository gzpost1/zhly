package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.pay.service.service.config.NormalWeChatConfig;
import cn.cuiot.dmp.pay.service.service.dto.*;
import cn.cuiot.dmp.pay.service.service.entity.PayOrderEntity;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.enums.TradeChannelEnum;
import cn.cuiot.dmp.pay.service.service.vo.CreateOrderAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryParam;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.SettleInfoEntity;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;


/**
 * @author huq
 * @ClassName OrderAtService
 * @Date 2024/1/10 16:48
 **/
@Slf4j
@Service
public class OrderPayAtHandler {

    @Autowired
    private SysPayChannelSettingService sysPayChannelSettingService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private AbstractStrategyChoose choose;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private NormalWeChatConfig weChatConfig;
    /**
     * 渠道下预付单（线上支付加订单码支付）
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResp makeOrder(CreateOrderReq param) {
        param.validData();
        PayOrderEntity orderEntityOld = payOrderService.queryForDetailByOutOrderId(param.getOutOrderId(), null, OrderStatusEnum.CAN_PAY_STATUS);
        if (orderEntityOld != null) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "该订单已生成，请勿重复下单");
        }
        // 初始化订单以及子单信息并保存
        PayOrderEntity orderEntity = PayOrderEntity.initOrderEntity(param,weChatConfig);
        payOrderService.save(orderEntity);

        // 进行预下单
        List<CreateOrderParam.SubOrderItem> subOrderItems = Lists.newArrayList();
        param.getSubOrderItems().forEach(item -> {
            CreateOrderParam.SubOrderItem subOrderItem = new CreateOrderParam.SubOrderItem();
            BeanMapper.copy(item, subOrderItem);
            subOrderItem.setSettleInfo(SettleInfoEntity.builder().profitSharing(Boolean.FALSE).build());
            subOrderItems.add(subOrderItem);

        });
        CreateOrderParam createOrderParam = CreateOrderParam.builder()
                .appId(param.getAppId())
                .openId(param.getOpenId())
                .orderId(orderEntity.getOrderId().toString())
                .spbillCreateIp(param.getSpbillCreateIp())
                .totalFee(param.getTotalFee())
                .tradeType(TradeChannelEnum.getPayCode(param.getTradeType()))
                .subOrderItems(subOrderItems)
                .build();
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(param.getPayChannel(), param.getMchType()));
        CreateOrderAggregate createOrderAggregate = payBaseInterface.prePay(createOrderParam);
        CreateOrderResp orderResp = BeanMapper.map(createOrderAggregate, CreateOrderResp.class);
        orderResp.setOrderId(createOrderAggregate.getOrderId().toString());
        orderResp.setOutOrderId(param.getOutOrderId());
        return orderResp;
    }

    /**
     * 渠道查单
     *
     * @param param
     * @return
     */
    public PayOrderQueryResp queryOrder(PayOrderQueryReq param) {
        PayOrderEntity orderEntity = payOrderService.queryForDetailByOutOrderId(null,
                param.getOrderId(),
                null);
        if (orderEntity == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "订单不存在");
        }

        PayOrderQueryResp orderQueryResp = PayOrderQueryResp.initReturn(orderEntity);
        // 完结状态(已取消、支付成功、支付失败)的订单，直接返回数据
        if (OrderStatusEnum.PARENT_FINISHI_STATUS.contains(orderEntity.getStatus())) {
            return orderQueryResp;
        }



        PayOrderQueryParam queryParam = PayOrderQueryParam.initPayOrderQueryParam(orderEntity.getOrderId(), orderEntity,orderEntity.getPayMchId());
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(orderEntity.getPayChannel(), orderEntity.getMchType()));
        // 查询订单实际支付状态
        PayOrderQueryAggregate queryBO = payBaseInterface.orderQuery(queryParam);
        if (queryBO.getStatus().equals(orderEntity.getStatus())) {
            // 状态都没变，直接返回
            return orderQueryResp;
        }
        if (OrderStatusEnum.PAID.getStatus().equals(queryBO.getStatus())) {
            //支付成功通知结算报表 todo
            paySuccessCallbackHandler(orderEntity);
            orderQueryResp.setSubOrderItems(BeanMapper.mapList(queryBO.getSubOrderItems(), PayOrderQueryResp.SubOrderItem.class));
            orderQueryResp.setStatus(queryBO.getStatus());
            orderQueryResp.setPayCompleteTime(queryBO.getPayCompleteTime());
            orderQueryResp.setPayChannel(orderEntity.getPayChannel());
            orderQueryResp.setPayMethod(queryBO.getPayMethod());
            orderQueryResp.setPayCharge(queryBO.getPayCharge());
        }
        return orderQueryResp;
    }

    /**
     * 渠道关单
     *
     * @param param
     */
    public void closeOrder(CloseOrderReq param) {
        param.validData();
        PayOrderEntity orderEntity = payOrderService.queryForDetailByOutOrderId(param.getOutOrderId(),
                param.getOrderId(),
                null);
        if (orderEntity == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "订单不存在");
        }

        if (OrderStatusEnum.PARENT_FINISHI_STATUS.contains(orderEntity.getStatus())) {
            throw new BusinessException(ResultCode.PAY_ORDER_NOT_SUPPORT, "该订单不支持取消");
        }
        PayOrderQueryParam queryParam = PayOrderQueryParam.initPayOrderQueryParam(orderEntity.getOrderId(), orderEntity,orderEntity.getPayMchId());
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(orderEntity.getPayChannel(), orderEntity.getMchType()));
        // 查询订单实际支付状态
        PayOrderQueryAggregate queryBO = payBaseInterface.orderQuery(queryParam);
        // 是否允许关单
        if (!OrderStatusEnum.NOT_CANCEL_STATUS.contains(queryBO.getStatus())) {
            payBaseInterface.closeOrder(CloseOrderParam.builder()
                    .orderId(String.valueOf(orderEntity.getOrderId()))
                    .payTime(orderEntity.getCreateTime())
                    .tradeType(orderEntity.getTradeType())
                    .subOrderItems(Lists.newArrayList(new  CloseOrderParam.CloseSubOrderItem(orderEntity.getOrderId(),orderEntity.getPayMchId())))
                    .build());
            cancel(orderEntity, "超时未支付取消");
        }

        if (OrderStatusEnum.PAID.getStatus().equals(queryBO.getStatus())) {
            //支付成功通知结算报表 todo
            paySuccessCallbackHandler(orderEntity);
        }

    }

    private void cancel(PayOrderEntity orderEntity, String reason) {
        TransactionStatus transaction = null;
        try {
            transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

            // 主单
            orderEntity.setStatus(OrderStatusEnum.CANCEL.getStatus());
            orderEntity.setRemark(reason);
            payOrderService.updateById(orderEntity);
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("取消订单更新数据库异常", ex);
            throw new BusinessException(ResultCode.ERROR, "系统异常，请稍后再试");
        }

    }

    /**
     * 支付成功通知处理
     */
    public void paySuccessCallbackHandler(PayOrderEntity orderEntity) {
        TransactionStatus transaction = null;
        try {
            transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
            orderEntity.setStatus(OrderStatusEnum.PAID.getStatus());
            payOrderService.updateById(orderEntity);
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            if (Objects.nonNull(transaction)) {
                transactionManager.rollback(transaction);
            }
            log.error("支付成功处理更新数据库异常", ex);
            throw new BusinessException(ResultCode.ERROR, "系统异常，请稍后再试");
        }
    }
}
