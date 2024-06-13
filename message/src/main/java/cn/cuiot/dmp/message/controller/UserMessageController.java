package cn.cuiot.dmp.message.controller;//	模板

import cn.cuiot.dmp.base.application.rocketmq.MsgChannel;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.bean.dto.UserMessageAcceptDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.message.config.MqMsgChannel;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 9:47
 */
@RestController
@RequestMapping("/userMessage")
public class UserMessageController {

    @Autowired
    private MqMsgChannel msgChannel;

    @GetMapping("/im")
    public IdmResDTO<String> userMessageOutput() {
        UserMessageAcceptDto userMessageAcceptDto = new UserMessageAcceptDto();
        userMessageAcceptDto.setMsgType("1");
        userMessageAcceptDto.setMessage("测试消息");
        msgChannel.userMessageOutput().send(MessageBuilder.withPayload(userMessageAcceptDto)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
        return IdmResDTO.success(UUID.fastUUID().toString());
    }

}
