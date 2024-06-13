package cn.cuiot.dmp.archive.api.subscribe.mq;

import cn.cuiot.dmp.archive.infrastructure.config.ArchiveMsgChannel;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 档案中心消息消费
 *
 * @author: wuyongchong
 * @date: 2024/6/4 18:24
 */
@Slf4j
@Component
@EnableBinding(value = {ArchiveMsgChannel.class})
public class ArchiveMqConsumer {

    @StreamListener(ArchiveMsgChannel.ARCHIVE_INPUT)
    public <T> void archiveInputProccess(Message<SimpleMsg<T>> message) {
        log.info("ArchiveMqConsumer archiveInputProccess,tag:{},messageId:{},payload:{}",
                message.getHeaders().get(MessageConst.PROPERTY_TAGS),
                message.getHeaders().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID),
                message.getPayload());
    }

}
