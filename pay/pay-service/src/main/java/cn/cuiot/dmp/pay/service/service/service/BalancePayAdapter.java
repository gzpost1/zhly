package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.CloseOrderParam;
import cn.cuiot.dmp.pay.service.service.dto.CreateOrderParam;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.enums.BalanceChangeTypeEnum;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.vo.CreateOrderAggregate;
import cn.cuiot.dmp.pay.service.service.vo.BalanceEventAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 余额支付适配层
 */
@Service
@Slf4j
public class BalancePayAdapter implements IPayBaseInterface {

    @Autowired
    private BalanceRuleHandler balancePayService;
    @Autowired
    private MbBalanceChangeRecordService changeRecordService;

    @Override
    public String mark() {
        return PayChannelEnum.BALANCE.getMark().getPayMark();
    }


    @Override
    public CreateOrderAggregate prePay(CreateOrderParam param) {
        BalanceEventAggregate eventDto = BalanceEventAggregate.builder()
                .balance(param.getTotalFee())
                .orderId(param.getOutOrderId())
                .orderName(param.getProductName())
                .createTime(new Date())
                .changeType(BalanceChangeTypeEnum.BALANCE_PAY.getType())
                .houseId(param.getHouseId())
                .dateType(param.getDateType())
                .build();
        Long reportId = balancePayService.handler(eventDto);
        CreateOrderAggregate responseDto = new CreateOrderAggregate();
        responseDto.setTradeNo(reportId.toString());
        return responseDto;
    }


    @Override
    public IdmResDTO closeOrder(CloseOrderParam param) {
        return null;
    }

    @Override
    public PayOrderQueryAggregate orderQuery(PayOrderQueryParam param) {
        BalanceChangeRecord record =
                changeRecordService.selectOneByOrderId(Long.parseLong(param.getOutOrderId()),
                        BalanceChangeTypeEnum.BALANCE_PAY.getType());
        PayOrderQueryAggregate orderVo = new PayOrderQueryAggregate();
        orderVo.setOutOrderId(param.getOutOrderId());
        if (record == null) {
            // 支付失败
            orderVo.setThirdStatus("PAYERROR");
            orderVo.setStatus(OrderStatusEnum.PAY_FAILED.getStatus());
            orderVo.setStatus(OrderStatusEnum.PAY_FAILED.getStatus());
        } else {
            // 支付成功
            orderVo.setTotalFee(Math.abs(record.getBalance()));
            orderVo.setStatus(OrderStatusEnum.PAID.getStatus());
            orderVo.setPayOrderId(record.getId().toString());
            orderVo.setPayCompleteTime(record.getCreateTime());
            orderVo.setThirdStatus("SUCCESS");
            orderVo.setStatus(OrderStatusEnum.PAID.getStatus());
        }
        return orderVo;
    }


}
