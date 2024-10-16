package cn.cuiot.dmp.pay.service.service.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 接收消息队列名称
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface PayMsgBaseChannel {
    String PAYSUCCESSINPUT = "paySuccessInput";


    @Input(PAYSUCCESSINPUT)
    SubscribableChannel paySuccessInput();
}
