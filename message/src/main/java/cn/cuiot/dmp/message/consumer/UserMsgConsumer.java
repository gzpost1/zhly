package cn.cuiot.dmp.message.consumer;//	模板

import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.conver.UserMessageConvert;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:25
 */
@Component
@Slf4j
public class UserMsgConsumer {

    @Autowired
    private UserMessageService userMessageService;

    @StreamListener(MqMsgChannel.USERMESSAGEINPUT)
    public void userMessageConsumer(@Payload UserMessageAcceptDto userMessageAcceptDto) {
        log.info("userMessageInput:{}", userMessageAcceptDto);
        UserMessageEntity userMessage = UserMessageConvert.INSTANCE.concert(userMessageAcceptDto);
        userMessage.init();
//        List<UserMessageEntity> userMessageEntities = dealMsgByType(userMessage, userMessageAcceptDto);
//        userMessageService.saveBatch(userMessageEntities);
    }

    //    @Bean
//    public Consumer<Message<SimpleMsg>> userMessageConsumer() {
//        return userMessageAcceptDto -> {
//            log.info("userMessageInput:{}", userMessageAcceptDto);
////            UserMessageEntity userMessage = UserMessageConvert.INSTANCE.concert(userMessageAcceptDto);
////            userMessage.init();
////            dealMsgByType(userMessage, userMessageAcceptDto);
//        };
    @Bean
    public Consumer<Message<SimpleMsg>> userMessageConsumer() {
        return msg -> {
            log.info(Thread.currentThread().getName() + " Consumer1 Receive New Messages: " + msg.getPayload().getData());
        };
    }


    //TODO
    private List<UserMessageEntity> dealMsgByType(UserMessageEntity userMessage, UserMessageAcceptDto userMessageAcceptDto) {
        List<UserMessageEntity> userMessageEntities = new ArrayList<>();
        //根据消息类型处理消息
        switch (userMessageAcceptDto.getMsgType()) {
            case MsgTypeConstant.NOTICE:
                userMessageAcceptDto.getAcceptors().forEach(acceptor -> {
                    UserMessageEntity userMessageEntity = BeanUtil.copyProperties(userMessage, UserMessageEntity.class);
                    userMessageEntity.setAccepter(acceptor);
                    userMessageEntities.add(userMessageEntity);
                });
                break;
            case "2":
                //处理消息类型2
                break;
            default:
                break;
        }
        return userMessageEntities;
    }


}
