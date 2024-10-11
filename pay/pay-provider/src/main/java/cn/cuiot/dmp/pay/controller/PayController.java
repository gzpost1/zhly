package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.dto.*;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 渠道调用支付
 *
 * @author huq
 * @ClassName DemoController
 * @Date 2022/6/6 19:17
 **/
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderPayAtHandler orderPayAtHandler;


    /**
     * 预下单
     */
    @PostMapping("/makeOrder")
    public IdmResDTO<CreateOrderResp> pay(@RequestBody @Valid CreateOrderReq request) {
        log.info("拿到的数据：{}", JsonUtil.writeValueAsString(request));
        return IdmResDTO.success(orderPayAtHandler.makeOrder(request));
    }


    /**
     * 关单接口
     */
    @PostMapping("/close")
    public IdmResDTO closeOrder(@RequestBody @Valid CloseOrderReq request) {
        log.info("拿到的数据：{}", JsonUtil.writeValueAsString(request));
        orderPayAtHandler.closeOrder(request);
        return IdmResDTO.success();
    }

    /**
     * 查单接口
     */
    @PostMapping("/query")
    public IdmResDTO<PayOrderQueryResp> queryOrder(@RequestBody @Valid PayOrderQueryReq request) {
        return IdmResDTO.success(orderPayAtHandler.queryOrder(request));
    }



}
