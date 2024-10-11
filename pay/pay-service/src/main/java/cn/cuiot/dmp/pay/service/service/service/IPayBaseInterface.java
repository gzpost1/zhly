package cn.cuiot.dmp.pay.service.service.service;


import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.CloseOrderParam;
import cn.cuiot.dmp.pay.service.service.dto.CreateOrderParam;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.vo.CreateOrderAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryParam;

/**
 * 支付流程接口
 */
public interface IPayBaseInterface extends AbstractExecuteStrategy {
    void initSetting(SysPayChannelSetting payChannelSetting);
    /**
     * 预支付订单接口
     *
     * @param param
     * @return
     */
    CreateOrderAggregate prePay(CreateOrderParam param);

    /**
     * 关闭预订单接口
     *
     * @param param
     * @return
     */
    IdmResDTO closeOrder(CloseOrderParam param);

    /**
     * 查询订单
     *
     * @param param
     * @return
     */
    PayOrderQueryAggregate orderQuery(PayOrderQueryParam param);
}
