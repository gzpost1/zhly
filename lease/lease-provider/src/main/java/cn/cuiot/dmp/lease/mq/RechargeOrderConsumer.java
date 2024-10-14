package cn.cuiot.dmp.lease.mq;

import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.lease.rocketmq.IMqMemberConsumerConfig;
import cn.cuiot.dmp.lease.service.balance.RechargeOrderPayRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * @author huq
 * @ClassName MemberConsumer
 * @Date 2023/11/17 9:53
 **/
@Slf4j
@Service
@EnableBinding(IMqMemberConsumerConfig.class)
public class RechargeOrderConsumer {

    @Autowired
    private RechargeOrderPayRule orderPayRule;

    @StreamListener(IMqMemberConsumerConfig.MB_RECHARGE_ORDER_MESSAGE_INPUT)
    public <T> void archiveInputProccess(Message<SimpleMsg<T>> message) {
        log.info("payTimeout,tag:{},messageId:{},payload:{}",
                message.getHeaders().get(MessageConst.PROPERTY_TAGS),
                message.getHeaders().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID),
                message.getPayload());
        Long orderId = Long.valueOf(String.valueOf(message.getPayload().getData()));
        try {
            orderPayRule.payOvertimeHandler(orderId);
        } catch (Exception ex) {
            log.warn("订单【{}】超时处理失败，原因：{}", orderId, ex.getMessage());
        }
    }


}
