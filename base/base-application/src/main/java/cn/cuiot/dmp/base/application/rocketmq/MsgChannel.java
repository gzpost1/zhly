package cn.cuiot.dmp.base.application.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MsgChannel {
    /**
     * 日志发送
     */
    String OPERATIONLOGOUTPUT = "operationlogOutput";

    @Output(OPERATIONLOGOUTPUT)
    MessageChannel operationlogOutput();
}
