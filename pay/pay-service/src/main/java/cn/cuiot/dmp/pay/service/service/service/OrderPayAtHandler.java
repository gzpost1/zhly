package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.pay.service.service.dto.*;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.enums.WxOrderConstant;
import cn.cuiot.dmp.pay.service.service.rocketmq.PayChannel;
import cn.cuiot.dmp.pay.service.service.vo.CreateOrderAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryAggregate;
import cn.cuiot.dmp.pay.service.service.vo.PayOrderQueryParam;
import cn.cuiot.dmp.pay.service.service.vo.PaySuccessVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.normal.NormalQueryOrderResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;


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
    private AbstractStrategyChoose choose;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Autowired
    private PayChannel msgChannel;

    /**
     * 渠道下预付单（线上支付加订单码支付）
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResp makeOrder(CreateOrderReq param) {
        param.validData();
        CreateOrderParam createOrderParam = CreateOrderParam.initDate(param);
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(param.getPayChannel(), param.getMchType()));
        CreateOrderAggregate createOrderAggregate = payBaseInterface.prePay(createOrderParam);
        CreateOrderResp orderResp = BeanMapper.map(createOrderAggregate, CreateOrderResp.class);
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
        SysPayChannelSetting paySetting = sysPayChannelSettingService.getPaySettingByOrgId(param.getOrgId());
        PayOrderQueryParam queryParam = PayOrderQueryParam.initPayOrderQueryParam(param.getOutOrderId(), paySetting.getPayMchId(),param.getOrgId());
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(paySetting.getPayChannel(), paySetting.getMchType()));
        // 查询订单实际支付状态
        PayOrderQueryAggregate queryBO = payBaseInterface.orderQuery(queryParam);
        return PayOrderQueryResp.initReturn(queryBO,paySetting);
    }

    /**
     * 渠道关单
     *
     * @param param
     */
    public void closeOrder(CloseOrderReq param) {
        SysPayChannelSetting paySetting = sysPayChannelSettingService.getPaySettingByOrgId(param.getOrgId());
        IPayBaseInterface payBaseInterface = (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(paySetting.getPayChannel(), paySetting.getMchType()));
        payBaseInterface.closeOrder(CloseOrderParam.builder()
                        .orgId(param.getOrgId())
                        .outOrderId(String.valueOf(param.getOutOrderId()))
                        .build());

    }



    /**
     * 微信普通商户模式支付通知消费
     */
    public void onReceiverWechatNormalPayNotify(String jsonStr,SysPayChannelSetting paySetting) {
        NormalQueryOrderResp order = JSON.parseObject(jsonStr,
                new TypeReference<NormalQueryOrderResp>() {
                });
        log.info("支付成功通知：" + JSON.toJSONString(order));
        //通知渠道
        PaySuccessVO paySuccessVO = PaySuccessVO.builder().outOrderId(order.getOutTradeNo())
                .transactionNo(order.getTransactionId())
                .status(WxOrderConstant.WePayOrderStatus.getYjStatus(order.getTradeState()))
                .businessType(StringUtils.isBlank(order.getAttach())?null:Byte.valueOf(order.getAttach()))
                .payRate(paySetting.getCharge())
                .build();
        sendDeviceInfo(paySuccessVO);

    }


    /**
     * 发送成功支付消息给渠道
     *
     * @param paySuccessVO 参数
     */
    public void sendDeviceInfo(PaySuccessVO paySuccessVO) {
        msgChannel.paySucessOutputOutput().send(messageBuilderObject(paySuccessVO));
    }

    public <T> Message<T> messageBuilderObject(T data) {
        return MessageBuilder.withPayload(data)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }
}
