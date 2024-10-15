package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.ChargePayDto;
import cn.cuiot.dmp.lease.dto.charge.ChargePayWechatResultDto;
import cn.cuiot.dmp.lease.service.charge.order.ChargePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 账单支付
 * @Description 账单支付
 * @Date 2024/10/11 17:33
 * @Created by libo
 */
@RestController
@RequestMapping("/appChargePay")
public class ChargePayController {
    @Autowired
    private ChargePayService chargePayService;

    /**
     * 微信支付
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/payByWechat")
    @LogRecord(operationCode = "payByWechat", operationName = "账单支付-微信支付", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
    public IdmResDTO<ChargePayWechatResultDto> payByWechat(@RequestBody @Valid ChargePayDto queryDto) {
        return IdmResDTO.success().body(chargePayService.payByWechat(queryDto));
    }

    /**
     * 取消支付，关闭微信预订单
     */
    @PostMapping("/cancelPay")
    @LogRecord(operationCode = "cancelPay", operationName = "账单支付-取消支付", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
    public IdmResDTO cancelPay(@RequestBody @Valid IdParam idParam) {
        chargePayService.cancelPay(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 预缴支付
     */
    @PostMapping("/prePay")
    @LogRecord(operationCode = "prePay", operationName = "账单支付-预缴支付", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
    public IdmResDTO prePay(@RequestBody @Valid ChargePayDto chargePayDto) {
        //房屋押金不能预缴支付
        AssertUtil.isFalse(Objects.equals(chargePayDto.getDataType(), Byte.valueOf("1")),"房屋押金不支持预缴支付");
        chargePayService.prePay(chargePayDto);
        return IdmResDTO.success();
    }
}
