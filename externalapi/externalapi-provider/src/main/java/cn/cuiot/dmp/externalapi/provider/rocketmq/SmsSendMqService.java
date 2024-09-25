package cn.cuiot.dmp.externalapi.provider.rocketmq;

import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * mq发送
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class SmsSendMqService {
    @Autowired
    private SmsMsgChannel msgChannel;

    /**
     * 发送格物消防-设备信息
     *
     * @param dto 参数
     */
    public void sendSmsPushData(SmsPushDataQuery dto) {
        msgChannel.smsPushDataOutput().send(messageBuilderObject(dto));
    }

    public <T> Message<T> messageBuilderObject(T data) {
        return MessageBuilder.withPayload(data)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }

}
