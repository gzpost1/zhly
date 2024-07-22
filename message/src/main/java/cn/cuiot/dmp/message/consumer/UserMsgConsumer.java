package cn.cuiot.dmp.message.consumer;//	模板

import cn.cuiot.dmp.common.bean.dto.SysMsgDto;
import cn.cuiot.dmp.common.bean.dto.UserBusinessMessageAcceptDto;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.InformTypeConstant;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.conver.UserMessageConvert;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 通知用户消费（针对相同模板不同参数）
     */
    @StreamListener(MqMsgChannel.USEBUSINESSRMESSAGEINPUT)
    public void userBusinessMessageConsumer(@Payload UserBusinessMessageAcceptDto dto) {
        log.info("userBusinessMessageInput:{}", JsonUtil.writeValueAsString(dto));
        if (Objects.equals(dto.getMsgType(), InformTypeConstant.SYS_MSG)) {
            if (CollectionUtils.isEmpty(dto.getSysMsgDto())) {
                return;
            }
            List<UserMessageEntity> userMessageEntities = dto.getSysMsgDto().stream().map(item -> {
                UserMessageEntity userMessage = new UserMessageEntity();
                BeanUtils.copyProperties(item, userMessage);
                userMessage.init();
                return userMessage;
            }).collect(Collectors.toList());
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
