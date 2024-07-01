package cn.cuiot.dmp.message.consumer;//	模板

import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.InformTypeConstant;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.conver.UserMessageConvert;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        log.info("userMessageInput:{}", JsonUtil.writeValueAsString(userMessageAcceptDto));
        if ((userMessageAcceptDto.getMsgType() & InformTypeConstant.SYS_MSG) == InformTypeConstant.SYS_MSG) {
            if (userMessageAcceptDto.getSysMsgDto() == null) {
                return;
            }
            UserMessageEntity userMessage = UserMessageConvert.INSTANCE.concert(userMessageAcceptDto.getSysMsgDto());
            userMessage.init();
            List<UserMessageEntity> userMessageEntities = dealMsgByType(userMessage, userMessageAcceptDto.getSysMsgDto());
            userMessageService.saveBatch(userMessageEntities);
        }
    }

    //TODO
    private List<UserMessageEntity> dealMsgByType(UserMessageEntity userMessage, SysMsgDto sysMsgDto) {
        List<UserMessageEntity> userMessageEntities = new ArrayList<>();
        sysMsgDto.getAcceptors().forEach(acceptor -> {
            UserMessageEntity userMessageEntity = BeanUtil.copyProperties(userMessage, UserMessageEntity.class);
            userMessageEntity.setAccepter(acceptor);
            userMessageEntities.add(userMessageEntity);
        });
        return userMessageEntities;
    }
}
