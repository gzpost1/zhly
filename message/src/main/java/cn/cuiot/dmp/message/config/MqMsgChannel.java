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

    String USERMESSAGEINPUT = "userMessageInput";

    @Input(USERMESSAGEINPUT)
    SubscribableChannel userMessageInput();

    String USERMESSAGEOUTPUT = "userMessageOutput";

    @Output(USERMESSAGEOUTPUT)
    SubscribableChannel userMessageOutput();

    /**
     * 用户业务消息
     */
    String USEBUSINESSRMESSAGEINPUT = "userBusinessMessageInput";

    @Input(USEBUSINESSRMESSAGEINPUT)
    SubscribableChannel userBusinessMessageInput();
}