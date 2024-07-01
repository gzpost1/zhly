package cn.cuiot.dmp.lease.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface ChargeMsgChannel {
    String USERBUSINESSMESSAGEOUTPUT = "userBusinessMessageOutput";

    @Output(USERBUSINESSMESSAGEOUTPUT)
    SubscribableChannel userBusinessMessageOutput();
}