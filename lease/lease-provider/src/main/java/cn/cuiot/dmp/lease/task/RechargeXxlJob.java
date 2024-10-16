package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.enums.PayStatus;
import cn.cuiot.dmp.lease.service.balance.MbRechargeOrderService;
import cn.cuiot.dmp.lease.service.balance.RechargeOrderPayRule;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品定时任务
 *
 * @author liuhong
 */
@Component
@Slf4j
public class RechargeXxlJob {


    @Autowired
    private RechargeOrderPayRule orderPayRule;
    @Autowired
    private MbRechargeOrderService orderService;

    @XxlJob("rechargePayTimeoutSyncJobHandler")
    public ReturnT<String> rechargePayTimeoutSyncJobHandler(String param) {
        log.info("-----充值订单支付超时兜底任务开始------");
        LambdaQueryWrapper<MbRechargeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MbRechargeOrder::getPayStatus, PayStatus.P_TO_BE_PAY.getStatus());
        queryWrapper.le(MbRechargeOrder::getOrderTime, LocalDateTime.now().plusMinutes(-10));
        List<MbRechargeOrder> list = orderService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return ReturnT.SUCCESS;
        }
        list.forEach(order -> {
            try {
                orderPayRule.payOvertimeHandler(order);
            } catch (Exception ex) {
                log.warn("订单【{}】超时处理失败，原因：{}", order.getOrderId(), ex.getMessage());
            }

        });
        return ReturnT.SUCCESS;
    }
}

