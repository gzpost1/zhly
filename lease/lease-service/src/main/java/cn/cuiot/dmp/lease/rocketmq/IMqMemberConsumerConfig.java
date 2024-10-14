package cn.cuiot.dmp.lease.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: wuyongchong
 * @date: 2020/4/26 18:29
 */
public interface IMqMemberConsumerConfig {

    String MB_RECHARGE_ORDER_MESSAGE_INPUT = "mbRechargeOrderMessageInput";

    @Input(MB_RECHARGE_ORDER_MESSAGE_INPUT)
    SubscribableChannel mbRechargeOrderMessageInput();
}
