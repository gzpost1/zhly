package cn.cuiot.dmp.base.infrastructure.stream;

import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 消息发送器
 *
 * @author: wuyongchong
 * @date: 2024/6/4 15:21
 */
@Slf4j
@Component
public class StreamMessageSender {

    @Autowired
    private StreamBridge streamBridge;

    private final static String KEY_PREFIX = "KEY";

    /**
     * 发送普通消息
     */
    public <T> void sendGenericMessage(String bindingName, SimpleMsg<T> msg) {
        String messageId = IdWorker.get32UUID();
        Map<String, Object> headers = Maps.newHashMap();
        headers.put(MessageConst.PROPERTY_KEYS, KEY_PREFIX + messageId);
        headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, messageId);
        headers.put(MessageConst.PROPERTY_TAGS, msg.getOperateTag());
        if(Objects.nonNull(msg.getDelayTimeLevel())){
            headers.put(MessageConst.PROPERTY_DELAY_TIME_LEVEL,msg.getDelayTimeLevel());
        }
        Message<SimpleMsg<T>> data = new GenericMessage(msg, headers);
        streamBridge.send(bindingName, data);
    }

}
