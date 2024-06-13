package cn.cuiot.dmp.app.subscribe.mq;

import cn.cuiot.dmp.app.config.SystemMsgChannel;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 系统管理消息消费
 *
 * @author: wuyongchong
 * @date: 2024/6/4 18:24
 */
@Slf4j
@Configuration
@EnableBinding(value = {SystemMsgChannel.class})
public class SystemMqConsumer {

    @StreamListener(SystemMsgChannel.SYSTEM_INPUT)
    public <T> void systemInputProccess(@Payload SimpleMsg<T> payload,@Headers Map headers) {
        log.info("SystemMqConsumer systemInputProccess,tag:{},messageId:{},payload:{}",
                headers.get(MessageConst.PROPERTY_TAGS),
                headers.get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID),
                payload);
    }

}
