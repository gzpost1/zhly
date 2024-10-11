package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.lease.dto.charge.Chargeovertimeorderdto;
import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import cn.cuiot.dmp.lease.service.charge.TbChargeOrderService;
import cn.cuiot.dmp.lease.service.charge.order.AbstrChargePay;
import cn.cuiot.dmp.lease.service.charge.order.ChargePayService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2024/10/10 20:04
 * @Created by libo
 */
@Slf4j
@Component
public class ChargeOrderTask {
    private static final long PAGE_SIZE = 100l;

    @Autowired
    private List<AbstrChargePay> chargePayList;
    @Autowired
    private TbChargeOrderService chargeOrderService;
    @Autowired
    private ChargePayService chargePayService;

    /**
     * 每天生成计费任务
     *
     * @param param
     * @return
     */
    @XxlJob("queryOverTimeOrderAndClose")
    public ReturnT<String> createDayWork(String param) {
        //1 首先查询订单状态为未支付状态的订单，然后关闭订单，并修改订单状态为已取消
        log.info("开始执行订单超时未支付任务");
        for (AbstrChargePay abstrChargePay : chargePayList) {
            //所需统计的企业，分页
            int pageNo = 0;
            int totalProcess = 0;

            long count = abstrChargePay.queryNeedPayCount();
            if (count > 0) {
                while (totalProcess < count) {
                    IPage<Chargeovertimeorderdto> checkCompanyIPage = abstrChargePay.queryNeedPayPage(new Page<Chargeovertimeorderdto>(pageNo, PAGE_SIZE));
                    if (checkCompanyIPage.getTotal() <= 0 || CollectionUtils.isEmpty(checkCompanyIPage.getRecords())) {
                        return ReturnT.SUCCESS;
                    } else {
                        totalProcess += checkCompanyIPage.getRecords().size();

                        //2 首选关闭第三方的订单，再修改本地订单状态
                        //2.1 首先根据订单id分组找出所有的订单号，值为需要缴费的数据id
                        Map<Long, List<Long>> orderMap = checkCompanyIPage.getRecords().stream()
                                .collect(Collectors.groupingBy(
                                        Chargeovertimeorderdto::getOrderId, // 按照 orderId 分组
                                        Collectors.mapping(Chargeovertimeorderdto::getDataId, Collectors.toList()) // 收集 dataId 到 List 中
                                ));

                        List<TbChargeOrder> tbChargeOrders = chargeOrderService.listByIds(orderMap.keySet());

                        if (CollectionUtils.isNotEmpty(tbChargeOrders)) {
                            Map<Long, TbChargeOrder> orderIdAndOrderNoMap = tbChargeOrders.stream()
                                    .collect(Collectors.toMap(TbChargeOrder::getOrderId, Function.identity()));

                            orderMap
                                    .forEach((orderId, dataIds) -> {
                                        if (orderIdAndOrderNoMap.containsKey(orderId)) {
                                            TbChargeOrder tbChargeOrder = orderIdAndOrderNoMap.get(orderId);

                                            //取消支付
                                            chargePayService.cancelPay(dataIds, abstrChargePay, tbChargeOrder);
                                        }
                                    });
                        }
                    }
                }
            }
        }

        return ReturnT.SUCCESS;
    }

}
