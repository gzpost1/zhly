package cn.cuiot.dmp.base.application.rocketmq;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SenderService {

    @Resource
    private StreamBridge streamBridge;

    /**
     * 发送对象消息
     */
    public <T> void sendObject(T data,String channelName) {
        Message<T> msg = new GenericMessage<T>(data);
        streamBridge.send(channelName, msg);
    }
}