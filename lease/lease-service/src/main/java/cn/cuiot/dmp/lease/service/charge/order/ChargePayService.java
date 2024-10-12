package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import cn.cuiot.dmp.lease.service.charge.TbChargeOrderService;
import cn.cuiot.dmp.pay.service.service.dto.*;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.utils.AssertUtil.DEFAULT_ERROR_CODE;

/**
 * @Description 收费支付服务
 * @Date 2024/10/10 19:15
 * @Created by libo
 */
@Slf4j
@Service
public class ChargePayService {

    @Autowired
    private List<AbstrChargePay> chargePays;
    @Autowired
    private TbChargeOrderService chargeOrderService;
    @Autowired
    private OrderPayAtHandler orderPayAtHandler;

    /**
     * 微信支付
     *
     * @param queryDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ChargePayWechatResultDto payByWechat(@Valid ChargePayDto queryDto) {
        //更具数据类型获取对应支付服务
        AbstrChargePay chargePay = chargePays.stream()
                .filter(item -> item.getDataType().equals(queryDto.getDataType())).findFirst().orElseThrow(() -> new BusinessException(DEFAULT_ERROR_CODE, "业务不支持，请检查"));

        Long orderId = IdWorker.getId();

        List<Long> chargeIds = queryDto.getChargeIds();
        //1 修改收费项目状态，如果没有所以锁定相关数目则抛出错误
        int updateCount = chargePay.updateChargePayStatusToNeedPay(chargeIds, orderId);

        //2 创建订单
        List<ChargePayToWechatDetailDto> list = chargePay.queryForPayToWechat(chargeIds);
        AssertUtil.isTrue(updateCount != list.size() || chargeIds.size() != list.size(), "账单正在支付中，请勿重复操作");
        //求出总金额
        Integer totalAmount = list.stream().mapToInt(ChargePayToWechatDetailDto::getChargeAmount).sum();

        //插入流水
        TbChargeOrder tbChargeOrder = new TbChargeOrder();
        tbChargeOrder.setOrderId(orderId);
        tbChargeOrder.setOrderDetail(list);
        tbChargeOrder.setCreateTime(new Date());

        //调用支付服务
        CreateOrderReq createOrderReq = new CreateOrderReq();
        createOrderReq.setOutOrderId(orderId.toString());
        createOrderReq.setPayChannel(1);
        createOrderReq.setMchType(Byte.valueOf("1"));
        createOrderReq.setTradeType("01");
        createOrderReq.setOpenId(queryDto.getOpenId());
        createOrderReq.setTotalFee(totalAmount);
        List<CreateOrderReq.SubOrderItem> subOrderItems = new ArrayList<>();
        createOrderReq.setSubOrderItems(subOrderItems);
        CreateOrderReq.SubOrderItem subOrderItem = new CreateOrderReq.SubOrderItem();
        subOrderItem.setAttach(JsonUtil.writeValueAsString(tbChargeOrder));
        subOrderItem.setTotalFee(totalAmount);
        subOrderItem.setProductName("小程序用手动缴费");
        subOrderItems.add(subOrderItem);
        CreateOrderResp createOrderResp = orderPayAtHandler.makeOrder(createOrderReq);

        //构建返回值
        ChargePayWechatResultDto chargePayResultDto = new ChargePayWechatResultDto();
        chargePayResultDto.setOrderId(orderId);
        chargePayResultDto.setOrderJson(JsonUtil.writeValueAsString(createOrderResp.getUnifiedOrderSignRsp()));
        chargePayResultDto.setMerchantId(1L);

        tbChargeOrder.setPayId(Long.valueOf(createOrderResp.getOrderId()));
        tbChargeOrder.setCreateUser(LoginInfoHolder.getCurrentUserId());
        chargeOrderService.save(tbChargeOrder);

        return chargePayResultDto;
    }

    /**
     * 取消支付
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelPay(Long id) {
        TbChargeOrder order = chargeOrderService.getById(id);
        AssertUtil.isTrue(order == null, "订单不存在");

        //2 调用对应服务修改订单状态
        AbstrChargePay chargePay = chargePays.stream()
                .filter(item -> item.getDataType().equals(order.getDataType())).findFirst().orElseThrow(() -> new BusinessException(DEFAULT_ERROR_CODE, "业务不支持，请检查"));

        //取消订单
        cancelPay(order.getOrderDetail().stream().map(ChargePayToWechatDetailDto::getChargeId).collect(Collectors.toList()), chargePay, order);
    }

    /**
     * 取消订单
     *
     * @param dataIds
     * @param abstrChargePay
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelPay(List<Long> dataIds, AbstrChargePay abstrChargePay, TbChargeOrder order) {
        PayOrderQueryReq payOrderQueryReq = new PayOrderQueryReq();
        payOrderQueryReq.setOrderId(order.getPayId());
        PayOrderQueryResp payOrderQueryResp = orderPayAtHandler.queryOrder(payOrderQueryReq);

        boolean isPaySuccess = Objects.equals(payOrderQueryResp.getStatus(), OrderStatusEnum.PAID.getStatus());
        if (isPaySuccess) {
            //2.2 修改本地订单状态为已支付
            ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
            chargeOrderPaySuccInsertDto.setTransactionNo(payOrderQueryResp.getPayOrderId());
            chargeOrderPaySuccInsertDto.setOrderId(order.getOrderId());
            chargeOrderPaySuccInsertDto.setDataIds(dataIds);
            chargeOrderPaySuccInsertDto.setOrder(order);
            abstrChargePay.updateChargePayStatusToPaySuccess(chargeOrderPaySuccInsertDto);
        } else {
            //2.3 关闭订单
            CloseOrderReq closeOrderReq = new CloseOrderReq();
            closeOrderReq.setOrderId(order.getPayId());
            try {
                log.error("关闭订单，订单详细：{}",order);
                orderPayAtHandler.closeOrder(closeOrderReq);
                //关闭成功
                abstrChargePay.updateChargePayStatusToCancel(dataIds);
            }catch (Exception e){
                log.info("关闭订单失败,订单信息为{},错误为{}", order,e);
            }
        }
    }

    public Boolean queryOrder(){

    }

    /**
     * 支付成功处理
     * @param chargeOrderPaySuccInsertDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        TbChargeOrder order = chargeOrderService.getById(chargeOrderPaySuccInsertDto.getOrderId());
        AssertUtil.isTrue(order == null, "订单不存在");

        //2 调用对应服务修改订单状态
        AbstrChargePay chargePay = chargePays.stream()
                .filter(item -> item.getDataType().equals(order.getDataType())).findFirst().orElseThrow(() -> new BusinessException(DEFAULT_ERROR_CODE, "业务不支持，请检查"));
        //2.2 修改本地订单状态为已支付
        chargeOrderPaySuccInsertDto.setDataIds(order.getOrderDetail().stream().map(ChargePayToWechatDetailDto::getChargeId).collect(Collectors.toList()));
        chargeOrderPaySuccInsertDto.setOrder(order);

        chargePay.updateChargePayStatusToPaySuccess(chargeOrderPaySuccInsertDto);

    }
}
