package cn.cuiot.dmp.lease.controller.balance;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.lease.dto.balance.MbRechargeOrderCreateDto;
import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.service.balance.RechargeOrderPayRule;
import cn.cuiot.dmp.lease.vo.balance.MbRechargeOrderCreateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 移动端-充值订单
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@RestController
@RequestMapping("/balance/house-recharge-order")
public class MbRechargeOrderController {

    @Autowired
    private RechargeOrderPayRule orderPayRule;
    /**
     * 充值订单下单
     *
     * @return
     */
    @PostMapping("/placeOrder")
    public IdmResDTO<MbRechargeOrderCreateVo> placeOrder(@RequestBody @Valid MbRechargeOrderCreateDto param) {
        MbRechargeOrder order = BeanMapper.map(param, MbRechargeOrder.class);
        MbRechargeOrderCreateVo createVo = orderPayRule.createOrder(param, order);
        return IdmResDTO.success(createVo);
    }




}
