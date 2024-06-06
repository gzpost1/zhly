package cn.cuiot.dmp.system.api.subscribe.mq;

import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.system.infrastructure.config.SystemMsgChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 系统管理消息消费
 *
 * @author: wuyongchong
 * @date: 2024/6/4 18:24
 */
@Slf4j
@Component
@EnableBinding(value = {SystemMsgChannel.class})
public class SystemMqConsumer {

    @StreamListener(SystemMsgChannel.SYSTEM_INPUT)
    public <T> void systemInputProccess(Message<SimpleMsg<T>> message) {
        log.info("SystemMqConsumer systemInputProccess,tag:{},messageId:{},payload:{}",
                message.getHeaders().get(MessageConst.PROPERTY_TAGS),
                message.getHeaders().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID),
                message.getPayload());
    }

}
