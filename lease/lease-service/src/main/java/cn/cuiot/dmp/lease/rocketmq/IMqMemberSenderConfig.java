package cn.cuiot.dmp.lease.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author: wuyongchong
 * @date: 2020/4/26 18:29
 */
public interface IMqMemberSenderConfig {

    String MB_RECHARGE_ORDER_MESSAGE_OUTPUT = "mbRechargeOrderMessageOutput";


    @Output(MB_RECHARGE_ORDER_MESSAGE_OUTPUT)
    MessageChannel mbRechargeOrderMessageOutput();
}
