package cn.cuiot.dmp.system.api.subscribe.mq;

import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.context.annotation.Bean;
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
public class SystemMqConsumer {

    /**
     * 消息处理
     */
    @Bean
    public Consumer<Message<SimpleMsg>> systemConsumer() {
        return msg -> {
            log.info(Thread.currentThread().getName()
                            + "systemConsumer Receive Messages,tag:{},messageId:{},payload:{}",
                    msg.getHeaders().get(MessageConst.PROPERTY_TAGS),
                    msg.getHeaders().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID),
                    msg.getPayload());
        };
    }

}
