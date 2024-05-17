package cn.cuiot.dmp.baseconsumer.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Description 接收消息队列名称
 * @Date 2024/5/8 11:58
 * @Created by libo
 */
public interface MsgBaseChannel {
    /**
     * 日志发送
     */
    String OPERATIONLOGINPUT = "operationlogInput";

    @Input(OPERATIONLOGINPUT)
    SubscribableChannel operationlogInput();
}
