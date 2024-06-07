package cn.cuiot.dmp.message.config;//	模板

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:11
 */
public interface MqMsgChannel {

    String USERMESSAGEINPUT = "userMessageConsumer-in-0";

    @Input(USERMESSAGEINPUT)
    SubscribableChannel userMessageInput();

}