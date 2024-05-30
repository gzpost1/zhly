package cn.cuiot.dmp.message.consumer;//	模板

import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.param.UserMessageAcceptDto;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.cuiot.dmp.message.conver.UserMessageConver;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:25
 */
@Service
@Slf4j
public class UserMsgConsumer {

    @Autowired
    private UserMessageService userMessageService;

    @StreamListener(MqMsgChannel.USERMESSAGEINPUT)
    public void userMessageInput(@Payload UserMessageAcceptDto userMessageAcceptDto) {
        log.info("userMessageInput:{}", userMessageAcceptDto);
        UserMessageEntity userMessage = UserMessageConver.INSTANCE.concer(userMessageAcceptDto);
        userMessage.init();
//        dealMsgByType(userMessage, userMessageAcceptDto);
    }
    @Bean
    public Consumer<UserMessageAcceptDto> userMessageInput() {
        return userMessageAcceptDto -> {
            log.info("userMessageInput:{}", userMessageAcceptDto);
            UserMessageEntity userMessage = UserMessageConver.INSTANCE.concer(userMessageAcceptDto);
            userMessage.init();
//            dealMsgByType(userMessage, userMessageAcceptDto);
        };
//        log.info("userMessageInput:{}", userMessageAcceptDto);
//        UserMessageEntity userMessage = UserMessageConver.INSTANCE.concer(userMessageAcceptDto);
//        userMessage.init();
//        dealMsgByType(userMessage, userMessageAcceptDto);
    }


    //TODO
    private void dealMsgByType(UserMessageEntity userMessage, UserMessageAcceptDto userMessageAcceptDto) {
        //根据消息类型处理消息
        switch (userMessageAcceptDto.getMsgType()) {
            case "1":
                //处理消息类型1
                break;
            case "2":
                //处理消息类型2
                break;
            default:
                break;
        }
    }
}
